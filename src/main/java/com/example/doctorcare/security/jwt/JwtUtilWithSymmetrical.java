package com.example.doctorcare.security.jwt;

import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Date;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;

import com.example.doctorcare.security.custom.UserDetailsCustom;
import com.example.doctorcare.utils.Const.TIME;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;

public class JwtUtilWithSymmetrical {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtilWithSymmetrical.class);

    // Symmetrical Key Using genarate random key
    SignatureAlgorithm algorithm = SignatureAlgorithm.HS256;
    SecretKey key = Keys.secretKeyFor(algorithm);

    // Using for login // OLD code of jwt symmetrical
    public String generateJwt(Authentication authentication) {
        UserDetailsCustom userAuthenticated = (UserDetailsCustom) authentication.getPrincipal();
        return Jwts.builder().setSubject((userAuthenticated.getUsername())).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + TIME.EXPIRE_DURATION_JWT))// 6h
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    // Using for change password
    public String generateTokenFromUsername(String email) {

        logger.info("Key calling form generateTokenFromUsername : " + key.toString());

        return Jwts.builder().setSubject(email).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + TIME.EXPIRE_DURATION_JWT_CHANGE_PASSWORD)) // 15m
                .signWith(key, SignatureAlgorithm.HS256).compact();
    }

    // Using for change password: Giải mã JWT và lấy thông tin claims
    public String getUserNameFromJwt(String toke) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(toke).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken)
            throws SignatureException,
            NoSuchAlgorithmException, java.io.IOException {
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

}
