package com.example.doctorcare.domain.system.user;

public enum UserType {
    SUPERADMIN("Quản trị viên hệ thống"), // Quản trị viên hệ thống
    MANAGER("Quản lý các đầu nhành"), 
    DOCTOR("Bác sỹ"),
    EMPLOYEE("Nhân viên - túc trực xét duyệt các cuộc hẹn - trả lời tin nhắn ..."),
    PATIENT("Bệnh nhân"),
    GUEST("Khách vãng lai - Đăng ký khám cho người khác.");            

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
