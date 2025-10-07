package com.example.doctorcare.infrastructure.security.domain.login;

import com.example.doctorcare.core.cqrs.CommandWithResult;
import jakarta.validation.constraints.NotNull;

public record LoginCommand(
    @NotNull String email,
    @NotNull String password) 
     implements CommandWithResult<LoginSuccessDetailDto> {
}
