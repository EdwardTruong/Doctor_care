package com.example.doctorcare.infrastructure.validation;


import java.lang.reflect.RecordComponent;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SignUpPasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

	@Override
	public void initialize(PasswordMatches constraintAnnotation) {
	}

	@Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        try {
            // Tìm field password
            var password = getValue(obj, "password");
            var rePassword = getValue(obj, "rePassword");

            if (password == null || rePassword == null) {
                return true; // để NotNull khác xử lý
            }
            return password.equals(rePassword);

        } catch (Exception e) {
            return false;
        }

	}

private Object getValue(Object record, String fieldName) throws Exception {
    if (record.getClass().isRecord()) {
        for (RecordComponent rc : record.getClass().getRecordComponents()) {
            if (rc.getName().equals(fieldName)) {
                return rc.getAccessor().invoke(record);
            }
        }
    } else {
        var field = record.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(record);
    }
    return null;
}
}
