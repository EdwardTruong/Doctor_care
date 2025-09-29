package com.example.doctorcare.infrastructure.security.old.auth.service.login;

import java.util.List;

public record JwtCommand (	
	 String token,
	 String username,
	 String type ,
	 Integer id,
	 List<String> roles
) {


}
