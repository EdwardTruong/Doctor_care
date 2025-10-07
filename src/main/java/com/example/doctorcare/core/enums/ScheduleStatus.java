package com.example.doctorcare.core.enums;

/**
 * Enum cho trạng thái của Schedule
 */
public enum ScheduleStatus {
    
    /**
     * Schedule có sẵn để đặt appointment
     */
    AVAILABLE("Available"),
    
    /**
     * Schedule đã hết chỗ (đã đạt maxAppointments)
     */
    FULLY_BOOKED("Fully Booked"),
    
    /**
     * Schedule bị hủy bởi bác sĩ
     */
    CANCELLED("Cancelled"),
    
    /**
     * Schedule đang tạm ngừng (ví dụ: bác sĩ nghỉ phép)
     */
    SUSPENDED("Suspended"),
    
    /**
     * Schedule đã hoàn thành (đã qua thời gian)
     */
    COMPLETED("Completed");
    
    private final String displayName;
    
    ScheduleStatus(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    /**
     * Check if this status allows new appointments
     */
    public boolean allowsNewAppointments() {
        return this == AVAILABLE;
    }
    
    /**
     * Check if this status is a final status (cannot be changed)
     */
    public boolean isFinal() {
        return this == COMPLETED || this == CANCELLED;
    }
}