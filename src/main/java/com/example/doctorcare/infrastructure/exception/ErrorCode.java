package com.example.doctorcare.infrastructure.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    
    // Generic errors
    INTERNAL_SERVER_ERROR(500, "INTERNAL_SERVER_ERROR", "Internal server error occurred"),
    INVALID_INPUT(400, "INVALID_INPUT", "Invalid input provided"),
    VALIDATION_FAILED(400, "VALIDATION_FAILED", "Input validation failed"),
    UNAUTHORIZED(401, "UNAUTHORIZED", "Unauthorized access"),
    FORBIDDEN(403, "FORBIDDEN", "Access forbidden"),
    BAD_REQUEST(400, "BAD_REQUEST", "Bad request"),
    
    // Resource errors
    RESOURCE_NOT_FOUND(404, "RESOURCE_NOT_FOUND", "Resource not found"),
    RESOURCE_ALREADY_EXISTS(409, "RESOURCE_ALREADY_EXISTS", "Resource already exists"),
    RESOURCE_IN_USE(409, "RESOURCE_IN_USE", "Resource is currently in use"),
    INVALID_OPERATION(400, "INVALID_OPERATION", "Invalid operation"),
    
    // User/Authentication errors
    USER_NOT_FOUND(404, "USER_NOT_FOUND", "User not found"),
    USER_ALREADY_EXISTS(409, "USER_ALREADY_EXISTS", "User already exists"),
    INVALID_CREDENTIALS(401, "INVALID_CREDENTIALS", "Invalid credentials"),
    ACCOUNT_LOCKED(423, "ACCOUNT_LOCKED", "Account is locked"),
    ACCOUNT_INACTIVE(403, "ACCOUNT_INACTIVE", "Account is inactive"),
    SESSION_EXPIRED(401, "SESSION_EXPIRED", "Session has expired"),
    INVALID_TOKEN(401, "INVALID_TOKEN", "Invalid token"),
    
    // Role/Permission errors
    ROLE_NOT_FOUND(404, "ROLE_NOT_FOUND", "Role not found"),
    ROLE_ALREADY_EXISTS(409, "ROLE_ALREADY_EXISTS", "Role already exists"),
    ROLE_IN_USE(409, "ROLE_IN_USE", "Role is currently in use"),
    INSUFFICIENT_PERMISSIONS(403, "INSUFFICIENT_PERMISSIONS", "Insufficient permissions"),
    
    // Manager specific errors
    MANAGER_NOT_FOUND(404, "MANAGER_NOT_FOUND", "Manager not found"),
    MANAGER_ALREADY_EXISTS(409, "MANAGER_ALREADY_EXISTS", "Manager already exists"),
    MANAGER_HAS_CHILDREN(409, "MANAGER_HAS_CHILDREN", "Manager has child managers"),
    MANAGER_MANAGING_DOCTORS(409, "MANAGER_MANAGING_DOCTORS", "Manager is managing doctors"),
    CIRCULAR_REFERENCE(400, "CIRCULAR_REFERENCE", "Circular reference detected in hierarchy"),
    INVALID_HIERARCHY(400, "INVALID_HIERARCHY", "Invalid hierarchy operation"),
    
    // Doctor specific errors
    DOCTOR_NOT_FOUND(404, "DOCTOR_NOT_FOUND", "Doctor not found"),
    DOCTOR_ALREADY_EXISTS(409, "DOCTOR_ALREADY_EXISTS", "Doctor already exists"),
    DOCTOR_NOT_AVAILABLE(409, "DOCTOR_NOT_AVAILABLE", "Doctor is not available"),
    DOCTOR_SCHEDULE_CONFLICT(409, "DOCTOR_SCHEDULE_CONFLICT", "Doctor schedule conflict"),
    
    // Patient specific errors
    PATIENT_NOT_FOUND(404, "PATIENT_NOT_FOUND", "Patient not found"),
    PATIENT_ALREADY_EXISTS(409, "PATIENT_ALREADY_EXISTS", "Patient already exists"),
    PATIENT_HAS_ACTIVE_APPOINTMENTS(409, "PATIENT_HAS_ACTIVE_APPOINTMENTS", "Patient has active appointments"),
    
    // Clinic specific errors
    CLINIC_NOT_FOUND(404, "CLINIC_NOT_FOUND", "Clinic not found"),
    CLINIC_ALREADY_EXISTS(409, "CLINIC_ALREADY_EXISTS", "Clinic already exists"),
    CLINIC_NOT_AVAILABLE(409, "CLINIC_NOT_AVAILABLE", "Clinic is not available"),
    
    // Appointment/Schedule errors
    APPOINTMENT_NOT_FOUND(404, "APPOINTMENT_NOT_FOUND", "Appointment not found"),
    APPOINTMENT_ALREADY_EXISTS(409, "APPOINTMENT_ALREADY_EXISTS", "Appointment already exists"),
    APPOINTMENT_TIME_CONFLICT(409, "APPOINTMENT_TIME_CONFLICT", "Appointment time conflict"),
    APPOINTMENT_CONFLICT(409, "APPOINTMENT_CONFLICT", "Appointment conflict detected"),
    APPOINTMENT_TIME_OUTSIDE_SCHEDULE(400, "APPOINTMENT_TIME_OUTSIDE_SCHEDULE", "Appointment time is outside schedule range"),
    APPOINTMENT_ALREADY_FINAL_STATUS(409, "APPOINTMENT_ALREADY_FINAL_STATUS", "Appointment already has final status"),
    CANNOT_UPDATE_FINAL_STATUS_APPOINTMENT(403, "CANNOT_UPDATE_FINAL_STATUS_APPOINTMENT", "Cannot update appointment with final status"),
    CANNOT_CANCEL_APPOINTMENT_TOO_LATE(403, "CANNOT_CANCEL_APPOINTMENT_TOO_LATE", "Cannot cancel appointment too close to appointment time"),
    INVALID_CANCELLATION_STATUS(400, "INVALID_CANCELLATION_STATUS", "Invalid cancellation status"),
    SCHEDULE_NOT_FOUND(404, "SCHEDULE_NOT_FOUND", "Schedule not found"),
    SCHEDULE_NOT_AVAILABLE(409, "SCHEDULE_NOT_AVAILABLE", "Schedule is not available"),
    SCHEDULE_FULLY_BOOKED(409, "SCHEDULE_FULLY_BOOKED", "Schedule is fully booked"),
    INVALID_APPOINTMENT_TIME(400, "INVALID_APPOINTMENT_TIME", "Invalid appointment time"),
    
    // Email specific errors
    EMAIL_ALREADY_EXISTS(409, "EMAIL_ALREADY_EXISTS", "Email already exists"),
    INVALID_EMAIL_FORMAT(400, "INVALID_EMAIL_FORMAT", "Invalid email format"),
    EMAIL_SEND_FAILED(500, "EMAIL_SEND_FAILED", "Failed to send email"),
    
    // File/Upload errors
    FILE_NOT_FOUND(404, "FILE_NOT_FOUND", "File not found"),
    FILE_UPLOAD_FAILED(500, "FILE_UPLOAD_FAILED", "File upload failed"),
    INVALID_FILE_FORMAT(400, "INVALID_FILE_FORMAT", "Invalid file format"),
    FILE_SIZE_EXCEEDED(413, "FILE_SIZE_EXCEEDED", "File size exceeded limit"),
    
    // Data validation errors
    DUPLICATE_ENTRY(409, "DUPLICATE_ENTRY", "Duplicate entry detected"),
    MISSING_REQUIRED_FIELD(400, "MISSING_REQUIRED_FIELD", "Missing required field"),
    INVALID_DATE_FORMAT(400, "INVALID_DATE_FORMAT", "Invalid date format"),
    INVALID_TIME_FORMAT(400, "INVALID_TIME_FORMAT", "Invalid time format"),
    INVALID_PHONE_FORMAT(400, "INVALID_PHONE_FORMAT", "Invalid phone number format"),
    
    // Business logic errors
    OPERATION_NOT_PERMITTED(403, "OPERATION_NOT_PERMITTED", "Operation not permitted"),
    BUSINESS_RULE_VIOLATION(400, "BUSINESS_RULE_VIOLATION", "Business rule violation"),
    CONCURRENT_MODIFICATION(409, "CONCURRENT_MODIFICATION", "Concurrent modification detected"),
    DATA_INTEGRITY_VIOLATION(409, "DATA_INTEGRITY_VIOLATION", "Data integrity violation");
    
    private final int httpStatus;
    private final String code;
    private final String message;
    
    /**
     * Get error code by code string
     */
    public static ErrorCode fromCode(String code) {
        for (ErrorCode errorCode : values()) {
            if (errorCode.code.equals(code)) {
                return errorCode;
            }
        }
        return INTERNAL_SERVER_ERROR;
    }
    
    /**
     * Check if error code is client error (4xx)
     */
    public boolean isClientError() {
        return httpStatus >= 400 && httpStatus < 500;
    }
    
    /**
     * Check if error code is server error (5xx)
     */
    public boolean isServerError() {
        return httpStatus >= 500;
    }
}