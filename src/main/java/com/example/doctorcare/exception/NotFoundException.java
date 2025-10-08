package com.example.doctorcare.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception for 404 Not Found errors
 * Used when requested resource does not exist
 */
public class NotFoundException extends BaseException {
    
    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "NOT_FOUND");
    }
    
    public NotFoundException(String message, String errorCode) {
        super(message, HttpStatus.NOT_FOUND, errorCode);
    }
    
    public NotFoundException(String message, Throwable cause) {
        super(message, HttpStatus.NOT_FOUND, "NOT_FOUND", cause);
    }
    
    public NotFoundException(String message, String errorCode, Throwable cause) {
        super(message, HttpStatus.NOT_FOUND, errorCode, cause);
    }
    
    // Specific not found scenarios for healthcare domain
    public static NotFoundException userNotFound(Long userId) {
        return new NotFoundException("User not found with ID: " + userId, "USER_NOT_FOUND");
    }
    
    public static NotFoundException userNotFound(String email) {
        return new NotFoundException("User not found with email: " + email, "USER_NOT_FOUND");
    }
    
    public static NotFoundException patientNotFound(Long patientId) {
        return new NotFoundException("Patient not found with ID: " + patientId, "PATIENT_NOT_FOUND");
    }
    
    public static NotFoundException doctorNotFound(Long doctorId) {
        return new NotFoundException("Doctor not found with ID: " + doctorId, "DOCTOR_NOT_FOUND");
    }
    
    public static NotFoundException appointmentNotFound(Long appointmentId) {
        return new NotFoundException("Appointment not found with ID: " + appointmentId, "APPOINTMENT_NOT_FOUND");
    }
    
    public static NotFoundException scheduleNotFound(Long scheduleId) {
        return new NotFoundException("Schedule not found with ID: " + scheduleId, "SCHEDULE_NOT_FOUND");
    }
    
    public static NotFoundException clinicNotFound(Long clinicId) {
        return new NotFoundException("Clinic not found with ID: " + clinicId, "CLINIC_NOT_FOUND");
    }
    
    public static NotFoundException specializationNotFound(Long specializationId) {
        return new NotFoundException("Specialization not found with ID: " + specializationId, "SPECIALIZATION_NOT_FOUND");
    }
    
    public static NotFoundException resourceNotFound(String resourceType, Object identifier) {
        return new NotFoundException(
            String.format("%s not found with identifier: %s", resourceType, identifier), 
            "RESOURCE_NOT_FOUND"
        );
    }
}