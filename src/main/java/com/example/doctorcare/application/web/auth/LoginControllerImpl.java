package com.example.doctorcare.application.web.auth;

import org.springframework.web.bind.annotation.RestController;
import com.example.doctorcare.infrastructure.security.domain.login.LoginCommand;
import com.example.doctorcare.infrastructure.security.domain.login.LoginCommandHandler;
import com.example.doctorcare.infrastructure.security.domain.login.LoginSuccessDetailDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class LoginControllerImpl implements LoginController {

        private final LoginCommandHandler loginCommandHandler;

        @Override
        public LoginSuccessDetailDto login(LoginCommand loginCommand) {
                log.debug("Creating new login for user: {}", loginCommand.email());
                return loginCommandHandler.handle(loginCommand);
        }

}
