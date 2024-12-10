package com.example.doctorcare.validation;

import com.example.doctorcare.model.dto.request.SignupRequest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SignUpPasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

	@Override
	public void initialize(PasswordMatches constraintAnnotation) {
	}

	@Override
	public boolean isValid(Object value, ConstraintValidatorContext context) {

		SignupRequest signup = (SignupRequest) value;
		
		return signup.getPassword().equals(signup.getRePassword());

	}

}
