package com.example.doctorcare.domain.system.role;

    
/**
 * Phân loại vai trò để xác định ngữ cảnh áp dụng chính của chúng. 
 * Thuận tiện cho Frontend filter các combobox vai trò ở các ngữ cảnh khác nhau
 * 
 */
public enum RoleType {
    /**
     * Vai trò liên quan đến một Doanh nhân.
     * Ví dụ: Vai trò hỗ trợ doanh nhân.
     */
    ENTREPRENEUR,

    /**
     * Vai trò được sử dụng trong phạm vi một Doanh nghiệp.
     * Ví dụ: Quản trị viên Doanh nghiệp, Nhân viên.
     */
    ENTERPRISE,

    /**
     * Vai trò được sử dụng trong phạm vi một Tổ chức (hiệp hội, CLB...).
     * Ví dụ: Quản trị viên Tổ chức, Quản lý Hội viên.
     */
    ORGANIZATION,

    /**
     * Vai trò có phạm vi toàn cục, không bị giới hạn bởi một doanh nghiệp hay tổ chức cụ thể.
     * Ví dụ: Quản trị viên Hệ thống (SUPER_ADMIN).
     */
    GLOBAL
}
