package com.example.doctorcare.application.service.appointment.query;

import java.time.LocalDateTime;

import org.springframework.data.domain.Pageable;

import com.example.doctorcare.core.enums.AppointmentStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Query để tìm kiếm appointments với filters phức tạp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetAppointmentsQuery {
    
    /**
     * ID của patient (filter)
     */
    Long patientId;
    
    /**
     * ID của doctor (filter)
     */
    Long doctorId;
    
    /**
     * ID của clinic (filter)
     */
    Long clinicId;
    
    /**
     * Trạng thái appointment (filter)
     */
    AppointmentStatus status;
    
    /**
     * Tìm kiếm theo tên patient
     */
    String patientName;
    
    /**
     * Thời gian bắt đầu (filter range)
     */
    LocalDateTime startTime;
    
    /**
     * Thời gian kết thúc (filter range)
     */
    LocalDateTime endTime;
    
    /**
     * Thông tin phân trang
     */
    Pageable pageable;
    
    /**
     * Kiểu view (minimal, full, dashboard)
     */
    @Builder.Default
    String viewType = "minimal";
}