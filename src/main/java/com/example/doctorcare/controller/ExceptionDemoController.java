package com.example.doctorcare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.doctorcare.exception.ExceptionUtils;
import com.example.doctorcare.exception.BadRequestException;
import com.example.doctorcare.exception.ForbiddenException;
import com.example.doctorcare.exception.NotFoundException;
import com.example.doctorcare.exception.ResponseException;
import com.example.doctorcare.exception.UnauthorizedException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Demo controller to showcase the custom exception handling system
 * This controller demonstrates various ways to throw and handle exceptions
 */
@Slf4j
@RestController
@RequestMapping("/api/demo/exceptions")
@RequiredArgsConstructor
public class ExceptionDemoController {

    @GetMapping("/bad-request")
    public ResponseEntity<String> testBadRequest() {
        throw new BadRequestException("This is a bad request example");
    }

    @GetMapping("/bad-request-custom")
    public ResponseEntity<String> testBadRequestCustom() {
        throw BadRequestException.invalidParameter("userId");
    }

    @GetMapping("/not-found")
    public ResponseEntity<String> testNotFound() {
        throw new NotFoundException("Resource not found example");
    }

    @GetMapping("/user-not-found/{userId}")
    public ResponseEntity<String> testUserNotFound(@PathVariable Long userId) {
        throw NotFoundException.userNotFound(userId);
    }

    @GetMapping("/patient-not-found/{patientId}")
    public ResponseEntity<String> testPatientNotFound(@PathVariable Long patientId) {
        ExceptionUtils.throwPatientNotFound(patientId);
        return ResponseEntity.ok("This won't be reached");
    }

    @GetMapping("/unauthorized")
    public ResponseEntity<String> testUnauthorized() {
        throw new UnauthorizedException("Unauthorized access example");
    }

    @GetMapping("/invalid-credentials")
    public ResponseEntity<String> testInvalidCredentials() {
        throw UnauthorizedException.invalidCredentials();
    }

    @GetMapping("/token-expired")
    public ResponseEntity<String> testTokenExpired() {
        ExceptionUtils.throwTokenExpired();
        return ResponseEntity.ok("This won't be reached");
    }

    @GetMapping("/forbidden")
    public ResponseEntity<String> testForbidden() {
        throw new ForbiddenException("Forbidden access example");
    }

    @GetMapping("/patient-access-denied/{patientId}")
    public ResponseEntity<String> testPatientAccessDenied(@PathVariable Long patientId) {
        throw ForbiddenException.patientAccessDenied(patientId);
    }

    @GetMapping("/admin-access-denied")
    public ResponseEntity<String> testAdminAccessDenied() {
        ExceptionUtils.throwAdminAccessDenied();
        return ResponseEntity.ok("This won't be reached");
    }

    @GetMapping("/response-conflict")
    public ResponseEntity<String> testResponseConflict() {
        throw ResponseException.conflict("This resource already exists");
    }

    @GetMapping("/validation-demo")
    public ResponseEntity<String> testValidation(@RequestParam String email, @RequestParam Long userId) {
        // Validate parameters using ExceptionUtils
        ExceptionUtils.validateEmail(email);
        ExceptionUtils.validatePositive(userId, "userId");
        
        return ResponseEntity.ok("Validation passed");
    }

    @PostMapping("/validation-object")
    public ResponseEntity<String> testObjectValidation(@RequestBody TestRequest request) {
        // Validate object fields
        ExceptionUtils.validateNotEmpty(request.getName(), "name");
        ExceptionUtils.validateNotNull(request.getAge(), "age");
        ExceptionUtils.validatePositive(request.getAge(), "age");
        
        return ResponseEntity.ok("Object validation passed");
    }

    @GetMapping("/permission-demo/{userId}")
    public ResponseEntity<String> testPermission(@PathVariable Long userId, 
                                                @RequestParam String currentUserRole,
                                                @RequestParam Long currentUserId) {
        // Check role permission
        String[] allowedRoles = {"ADMIN", "MANAGER"};
        ExceptionUtils.requireAnyRole(currentUserRole, allowedRoles, "view user data");
        
        // Check ownership for non-admin users
        if (!"ADMIN".equals(currentUserRole)) {
            ExceptionUtils.requireOwnership(currentUserId, userId, "user profile");
        }
        
        return ResponseEntity.ok("Permission check passed");
    }

    @GetMapping("/healthcare-demo/{patientId}")
    public ResponseEntity<String> testHealthcarePermissions(@PathVariable Long patientId,
                                                          @RequestParam String role,
                                                          @RequestParam Long doctorId) {
        
        // Simulate healthcare permission logic
        switch (role) {
            case "ADMIN":
                ExceptionUtils.throwAdminAccessDenied();
                break;
            case "DOCTOR":
                // Check if doctor is assigned to patient (mock logic)
                boolean isAssigned = checkDoctorAssignment(doctorId, patientId);
                if (!isAssigned) {
                    ExceptionUtils.throwDoctorNotAssigned(patientId);
                }
                break;
            case "PATIENT":
                // Patient can only access their own data (mock logic) 
                boolean isOwnData = checkPatientOwnership(doctorId, patientId); // doctorId used as currentUserId for demo
                if (!isOwnData) {
                    ExceptionUtils.throwPatientAccessDenied(patientId);
                }
                break;
            default:
                ExceptionUtils.throwForbidden("Invalid role: " + role);
        }
        
        return ResponseEntity.ok("Healthcare permission check passed for role: " + role);
    }

    @GetMapping("/null-check-demo/{id}")
    public ResponseEntity<String> testNullChecks(@PathVariable Long id) {
        // Simulate finding an entity (returns null)
        Object entity = findEntityById(id);
        
        // Use ExceptionUtils to check null and throw NotFoundException
        ExceptionUtils.throwIfNull(entity, "Entity not found with ID: " + id);
        
        // Or use requireNonNull to get the entity and throw if null
        Object checkedEntity = ExceptionUtils.requireNonNull(entity, "Entity is required");
        
        return ResponseEntity.ok("Entity found: " + checkedEntity);
    }

    @GetMapping("/condition-demo")
    public ResponseEntity<String> testConditions(@RequestParam int value) {
        // Throw if condition is true
        ExceptionUtils.throwIfTrue(value < 0, "Value cannot be negative");
        
        // Throw if condition is false
        ExceptionUtils.throwIfFalse(value <= 100, "Value must be 100 or less");
        
        return ResponseEntity.ok("Condition checks passed for value: " + value);
    }

    // Mock methods for demonstration
    private boolean checkDoctorAssignment(Long doctorId, Long patientId) {
        // Mock logic - in real implementation, check database
        return doctorId == 1L && patientId == 1L; // Only doctor 1 is assigned to patient 1
    }

    private boolean checkPatientOwnership(Long currentUserId, Long patientId) {
        // Mock logic - in real implementation, check if current user is the patient
        return currentUserId.equals(patientId);
    }

    private Object findEntityById(Long id) {
        // Mock method that returns null for ID > 10
        return id <= 10 ? new Object() : null;
    }

    // Test request DTO
    public static class TestRequest {
        private String name;
        private Long age;
        
        // Getters and setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public Long getAge() { return age; }
        public void setAge(Long age) { this.age = age; }
    }
}