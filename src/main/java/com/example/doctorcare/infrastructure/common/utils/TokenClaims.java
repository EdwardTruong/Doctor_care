package com.example.doctorcare.infrastructure.common.utils;

import lombok.Getter;
/**
 * Chưa sử dụng đến
 * version 2.0
 * Dùng để gáng các giá trị keyclock
 */
@Getter
public enum TokenClaims {

    JWT_ID("jti"), 
    USER_ID("userId"), 
    USER_STATUS("userStatus"), 
    USER_FIRST_NAME("userFirstName"), 
    USER_LAST_NAME("userLastName"), 
    USER_PERMISSIONS("userPermissions"),
    USER_EMAIL("userEmail"), 
    USER_PHONE_NUMBER("userPhoneNumber"), 
    STORE_TITLE("storeTitle"), 
    ISSUED_AT("iat"), 
    EXPIRES_AT("exp"), 
    ALGORITHM("alg"),
    TYP("typ"), 
    ROLE("role");

    private final String value;

    TokenClaims(String value) {
        this.value = value;
    }
}
