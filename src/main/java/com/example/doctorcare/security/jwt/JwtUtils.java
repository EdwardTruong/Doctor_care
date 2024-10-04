package com.example.doctorcare.security.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.example.doctorcare.security.custom.UserDetailsCustom;
import com.example.doctorcare.utils.Const;
import com.example.doctorcare.utils.Const.TIME;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

/*
 * 1. The field SECRET_KEY using text in application.properties (#5) to encode jwt 
 * 2. The method Key() return a hash-based Message Authentication Code.
 * 		- Decoders.BASE64.decode(SECRET_KEY) call decodes the base64-encoded string SECRET_KEY back into its original binary form represented as a byte[] array. 
 * 		- Keys.hmacShaKeyFor() create a key HMAC base on byte[] array of base64-encoded(SECRET_KEY).
 * 
 * 3. The generateJwtToken method using create a token when user logged in successfully.
 * 		-setSubject 	: The JWT Claims sub i use is username mean UserEntity's email 
 * 		-setIssuedAt  	: Date-time jwt was created
 * 		-setExpiration	: Date-time jwt expires
 * 		-signWith		: Sign the key() method with SignatureAlgorithm.HS256.
 * 							For now i didn't see another type of SignatureAlgorithm, so i use SignatureAlgorithm.HS256 
 * 							like https://jwt.io/introduction. 
 * 
 * 4. The getUserNameFromJwtToken method is get user's email into jwt.
 * 
 * 5. The validateJwt method using check Jwt.
 *  
 *  */

@Component
public class JwtUtils {

	@Value("${app.jwt.jwtCookieName}")
	private String jwtCookie;


	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	// key
	SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
	SecretKey key = Keys.secretKeyFor(algorithm);

	
	public String getUserNameFromJwtToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
	}


	// Using for login
	public String generateJwt(Authentication authentication) {

		UserDetailsCustom userAuthenticated = (UserDetailsCustom) authentication.getPrincipal();

		return Jwts.builder().setSubject((userAuthenticated.getUsername())).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + TIME.EXPIRE_DURATION_JWT))	// 6h
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	
	// Using for change password
	public String generateTokenFromUsername(String email) {

		logger.info("Key calling form generateTokenFromUsername : " + key.toString());

		return Jwts.builder().setSubject(email).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + TIME.EXPIRE_DURATION_JWT_CHANGE_PASSWORD)) // 15m
				.signWith(key, SignatureAlgorithm.HS256).compact();
	}

	//Using for change password:  Giải mã JWT và lấy thông tin claims 
	public String getUserNameFromJwt(String toke) {
		return Jwts.parserBuilder().setSigningKey(key).build()
				.parseClaimsJws(toke).getBody().getSubject();
	}

	public boolean validateJwtToken(String authToken) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parse(authToken);
			return true;
		} catch (MalformedJwtException e) {
			logger.error("Invalid JWT token: {}", e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("JWT token is unsupported: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims string is empty: {}", e.getMessage());
		}

		return false;
	}

	// FOR cookies . Make it later !
	public String getJwtFromCookies(HttpServletRequest request) {
		Cookie cookie = WebUtils.getCookie(request, jwtCookie);
		if (cookie != null) {
			return cookie.getValue();
		} else {
			return null;
		}
	}

	public ResponseCookie getCleanJwtCookie() {
		ResponseCookie cookie = ResponseCookie.from(jwtCookie, null).path("/api").build();
		return cookie;
	}

	public ResponseCookie generateJwtCookie(Authentication authentication) {
		String jwt = generateJwt(authentication);
		ResponseCookie cookie = ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(Const.TIME.EXPIRE_DURATION_JWT)
				.httpOnly(true) // 6h
				.build();
		return cookie;
	}

}
