package com.example.doctorcare.infrastructure.security.domain.login;

import java.util.List;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import lombok.Builder;

@Builder
public record LoginSuccessDetailDto(
	 String token,
	 String type,
	 Integer id,
	 String email,
	 List<Integer> rolesId
) {
}
