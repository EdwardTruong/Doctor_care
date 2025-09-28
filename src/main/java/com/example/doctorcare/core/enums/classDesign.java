package com.example.doctorcare.core.enums;

// Thiết kế hệ thống theo Role-Based Access Control (RBAC) 
public class classDesign {
    /**
     * Patient: chỉ có quyền VIEW/EDIT/DELETE:MEDICAL_RECORD:OWN và
     * CREATE:APPOINTMENT:OWN.
     * 
     * Doctor: VIEW:MEDICAL_RECORD:ASSIGNED, CREATE/EDIT/DELETE:SCHEDULE:OWN.
     * 
     * Manager: VIEW/EDIT:DOCTOR:MANAGED, VIEW/EDIT:PATIENT:MANAGED.
     * 
     * Super Admin: MANAGE:ALL:ANY.
     */
}
