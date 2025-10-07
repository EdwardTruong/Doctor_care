package com.example.doctorcare.core.enums;

/**
 * Status của cuộc hẹn khám bệnh
 */
public enum AppointmentStatus {
    /**
     * Chờ xác nhận từ bác sỹ
     */
    PENDING("Chờ xác nhận"),
    
    /**
     * Bác sỹ đã xác nhận cuộc hẹn
     */
    CONFIRMED("Đã xác nhận"),
    
    /**
     * Bác sỹ hủy cuộc hẹn
     */
    CANCELLED_BY_DOCTOR("Bác sỹ hủy"),
    
    /**
     * Bệnh nhân hủy cuộc hẹn
     */
    CANCELLED_BY_PATIENT("Bệnh nhân hủy"),
    
    /**
     * Đã hoàn thành khám bệnh
     */
    COMPLETED("Đã hoàn thành");

    private final String description;

    AppointmentStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}