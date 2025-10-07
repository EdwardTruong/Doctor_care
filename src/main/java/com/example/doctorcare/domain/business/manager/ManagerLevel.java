package com.example.doctorcare.domain.business.manager;

/**
 * Enum định nghĩa các cấp độ Manager trong hệ thống hierarchy
 */
public enum ManagerLevel {
    
    /**
     * Cấp cao nhất - Giám đốc/CEO
     */
    CEO(0, "CEO/Giám đốc", "Cấp cao nhất trong tổ chức"),
    
    /**
     * Phó giám đốc
     */
    VICE_CEO(1, "Phó Giám đốc", "Phó của CEO, hỗ trợ quản lý cấp cao"),
    
    /**
     * Giám đốc điều hành
     */
    GENERAL_MANAGER(1, "Giám đốc Điều hành", "Quản lý tổng thể hoạt động"),
    
    /**
     * Trưởng phòng/Quản lý cấp cao
     */
    DEPARTMENT_HEAD(2, "Trưởng phòng", "Quản lý một phòng ban cụ thể"),
    
    /**
     * Phó trưởng phòng
     */
    DEPUTY_HEAD(3, "Phó Trưởng phòng", "Phó của trưởng phòng"),
    
    /**
     * Team leader/Quản lý nhóm
     */
    TEAM_LEADER(4, "Trưởng nhóm", "Quản lý một nhóm làm việc"),
    
    /**
     * Supervisor - Giám sát viên
     */
    SUPERVISOR(5, "Giám sát viên", "Giám sát công việc hàng ngày"),
    
    /**
     * Manager cơ sở - không quản lý ai
     */
    STAFF_MANAGER(6, "Quản lý cơ sở", "Manager không có nhân viên dưới quyền");
    
    private final int level;
    private final String displayName;
    private final String description;
    
    ManagerLevel(int level, String displayName, String description) {
        this.level = level;
        this.displayName = displayName;
        this.description = description;
    }
    
    public int getLevel() {
        return level;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * Tìm ManagerLevel theo số level
     */
    public static ManagerLevel fromLevel(int level) {
        for (ManagerLevel managerLevel : values()) {
            if (managerLevel.level == level) {
                return managerLevel;
            }
        }
        return STAFF_MANAGER; // Default fallback
    }
    
    /**
     * Kiểm tra xem level này có cao hơn level khác không
     */
    public boolean isHigherThan(ManagerLevel other) {
        return this.level < other.level; // Level thấp hơn = cấp cao hơn
    }
    
    /**
     * Kiểm tra xem có phải là cấp cao (CEO, Vice CEO, General Manager) không
     */
    public boolean isExecutiveLevel() {
        return this.level <= 1;
    }
    
    /**
     * Kiểm tra xem có phải là cấp trung (Department Head, Deputy Head) không
     */
    public boolean isMiddleLevel() {
        return this.level >= 2 && this.level <= 3;
    }
    
    /**
     * Kiểm tra xem có phải là cấp cơ sở (Team Leader, Supervisor, Staff Manager) không
     */
    public boolean isLowerLevel() {
        return this.level >= 4;
    }
    
    @Override
    public String toString() {
        return displayName + " (Level " + level + ")";
    }
}