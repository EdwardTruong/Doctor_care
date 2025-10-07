package com.example.doctorcare.application.web.auth;

import org.springframework.web.bind.annotation.RequestMapping;
import com.example.doctorcare.infrastructure.security.domain.login.LoginCommand;
import com.example.doctorcare.infrastructure.security.domain.login.LoginSuccessDetailDto;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Login Management", description = "APIs login")
@RequestMapping("/api/v1/auth")
public interface LoginController {
    
    LoginSuccessDetailDto login(LoginCommand loginCommand);
}
