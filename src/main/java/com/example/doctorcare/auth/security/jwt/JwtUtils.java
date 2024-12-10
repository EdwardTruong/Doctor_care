package com.example.doctorcare.auth.security.jwt;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Date;

import javax.crypto.SecretKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.example.doctorcare.auth.security.custom.UserDetailsCustom;
import com.example.doctorcare.common.utils.Const;
import com.example.doctorcare.common.utils.Const.TIME;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.IOException;
import io.jsonwebtoken.security.Keys;

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
 *  
 */

/*
 * This is the second way i make jwt with asymmetrical type : 
 * Server sẽ sử dụng private key để tạo chữ ký cho JWT khi người dùng đăng nhập thành công và yêu cầu token.
 * JWT bao gồm payload chứa thông tin người dùng và một chữ ký được tạo bởi private key.
 * 
 *
 */

@Component
public class JwtUtils {

	@Value("${app.jwt.jwtCookieName}")
	private String jwtCookie;

	@Autowired
	KeyManger keyManger;

	private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

	// Asymetrical Key Using KeyPair.
	public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");// Sử dụng thuật toán RSA để tạo cặp
																				// khóa
		keyPairGenerator.initialize(2048); // Độ dài khóa 2048 bits
		return keyPairGenerator.generateKeyPair();
	}

	public String getUserNameFromJwtToken(String token)
			throws IOException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException,
			CertificateException, FileNotFoundException, java.io.IOException {
		PrivateKey privateKey = keyManger.getPrivateKey();
		return Jwts.parserBuilder().setSigningKey(privateKey).build().parseClaimsJws(token).getBody().getSubject();
	}

	/*
	 * New code for generateJwt using private key : PrivateKey privateKey =
	 * this.getPrivateKey(); and using SignatureAlgorithm.RS256 of RSA
	 */
	public String generateJwt(Authentication authentication)
			throws IOException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException,
			CertificateException, FileNotFoundException, java.io.IOException {

		PrivateKey privateKey = keyManger.getPrivateKey();

		SecretKey secretKey = Keys.hmacShaKeyFor(privateKey.getEncoded());

		UserDetailsCustom userAuthenticated = (UserDetailsCustom) authentication.getPrincipal();

		return Jwts.builder().setSubject((userAuthenticated.getUsername())).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + TIME.EXPIRE_DURATION_JWT)) // 6h
				.signWith(privateKey, SignatureAlgorithm.RS256).compact();
	}

	public PublicKey getPublicKey() throws KeyStoreException, IOException, UnrecoverableKeyException,
			NoSuchAlgorithmException, CertificateException,
			FileNotFoundException, java.io.IOException {
		KeyStore keyStore = KeyStore.getInstance(Const.KEY.KEY_STORE_CERTIFICATES);
		keyStore.load(new FileInputStream(Const.KEY.FILE_KEYTOOL_NAME), Const.KEY.KEYTOOL_PASSWORD.toCharArray());

		Certificate cert = keyStore.getCertificate(Const.KEY.KEYTOOL_ALIAS);
		PublicKey publicKey = cert.getPublicKey(); //
		return publicKey;
	}

	// Using for change password
	public String generateTokenFromUsername(String email)
			throws IOException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException,
			CertificateException, FileNotFoundException, java.io.IOException {

		PrivateKey privateKey = keyManger.getPrivateKey();
		logger.info("Key calling form generateTokenFromUsername : " + keyManger.toString());

		return Jwts.builder().setSubject(email).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + TIME.EXPIRE_DURATION_JWT_CHANGE_PASSWORD)) // 15m
				.signWith(privateKey, SignatureAlgorithm.RS256).compact();
	}

	// Using for change password: Giải mã JWT và lấy thông tin claims
	public String getUserNameFromJwt(String toke) throws IOException, UnrecoverableKeyException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, FileNotFoundException, java.io.IOException {
		PrivateKey privateKey = keyManger.getPrivateKey();
		return Jwts.parserBuilder().setSigningKey(
				privateKey).build()
				.parseClaimsJws(toke).getBody().getSubject();
	}

	/*
	 * This method using check private key is correct.
	 * If private key in signiture of claimsJwt dose not mathes into KeyStore then
	 * exception was throse
	 */
	public boolean validateJwtToken(String jwt)
			throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException,
			UnrecoverableKeyException, FileNotFoundException, java.io.IOException {
		PublicKey publicKey = this.getPublicKey();

		Jws<Claims> claimsJws = Jwts.parserBuilder()
				.setSigningKey(publicKey)
				.build()
				.parseClaimsJws(jwt);

		return true; // Nếu không có exception, JWT là hợp lệ
	}

	/*
	 * // FOR cookies . Make it later ! public String
	 * getJwtFromCookies(HttpServletRequest request) { Cookie cookie =
	 * WebUtils.getCookie(request, jwtCookie); if (cookie != null) { return
	 * cookie.getValue(); } else { return null; } }
	 * 
	 * public ResponseCookie getCleanJwtCookie() { ResponseCookie cookie =
	 * ResponseCookie.from(jwtCookie, null).path("/api").build(); return cookie; }
	 * 
	 * public ResponseCookie generateJwtCookie(Authentication authentication) {
	 * String jwt = generateJwt(authentication); ResponseCookie cookie =
	 * ResponseCookie.from(jwtCookie,
	 * jwt).path("/api").maxAge(Const.TIME.EXPIRE_DURATION_JWT) .httpOnly(true) //
	 * 6h .build(); return cookie; }
	 */

}
