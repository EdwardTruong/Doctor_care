package com.example.doctorcare.model.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Tạo thời sẽ trả Sẽ trả về JWT luôn mà không cần custom lớp nâng cao
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {

	private String token;
	private String type;
	private Integer id;
	private String email;
	private List<String> roles;

	// private String accessToken;
	// private Long accessTokenExpiresAt;
	// private String refreshToken;

	// private static final String TOKEN_PREFIX = "Bearer ";

	// /**
	// * Checks if the provided authorization header is a bearer token.
	// *
	// * @param authorizationHeader The authorization header string.
	// * @return {@code true} if the header is a bearer token; otherwise
	// * {@code false}.
	// */
	// public static boolean isBearerToken(final String authorizationHeader) {
	// return StringUtils.hasText(authorizationHeader) &&
	// authorizationHeader.startsWith(TOKEN_PREFIX);
	// }

	// /**
	// * Extracts the JWT token from the authorization header.
	// *
	// * @param authorizationHeader The authorization header string.
	// * @return The JWT token string extracted from the header.
	// */
	// public static String getJwt(final String authorizationHeader) {
	// return authorizationHeader.replace(TOKEN_PREFIX, "");
	// }

}
