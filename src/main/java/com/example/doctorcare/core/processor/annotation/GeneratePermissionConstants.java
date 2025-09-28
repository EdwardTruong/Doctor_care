package com.example.doctorcare.core.processor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.example.doctorcare.core.enums.Action;
import com.example.doctorcare.core.enums.Scope;


/**
 * Đánh dấu một enum (thường là {@code Resource}) để kích hoạt việc tự động sinh lớp hằng số Permissions.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
public @interface GeneratePermissionConstants {

    /**
     * Chỉ định các Action hợp lệ cho một hằng số enum Resource.
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.SOURCE)
    @interface SupportedActions {
        Action[] value();
    }

    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.SOURCE)
    @interface SupportedScopes {
        Scope[] value();
    }
}
