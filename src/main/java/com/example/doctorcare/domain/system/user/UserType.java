package com.example.doctorcare.domain.system.user;

public enum UserType {
    SUPERADMIN("Quản trị viên hệ thống"), // Quản trị viên hệ thống
    ENTREPRENEUR("Doanh nhân"), // Người dùng là Doanh nhân (liên kết với Entrepreneur entity)
    EMPLOYEE("Nhân viên"), // Người dùng là nhân viên của một doanh nghiệp hoặc một doanh nhân (được ủy quyền)
    CUSTOMER("Khách hàng"), // Người dùng là khách hàng (có thể là cá nhân hoặc tổ chức)
    PARTNER("Đối tác"), // Người dùng là đối tác (có thể là doanh nghiệp hoặc cá nhân)
    SPONSOR("Nhà tài trợ"), // Người dùng là nhà tài trợ (có thể là doanh nghiệp hoặc cá nhân)
    ADMINISTRATOR("Quản trị viên"), // Quản trị viên của một doanh nghiệp/tổ chức cụ thể
    GUEST("Khách vãng lai");            // Khách vãng lai (có thể đăng ký nhưng chưa có vai trò cụ thể)

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
