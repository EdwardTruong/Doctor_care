package com.example.doctorcare.application.service.appointment.command;

import java.time.LocalDateTime;

import com.example.doctorcare.core.enums.AppointmentStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Command để tạo appointment mới
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateAppointmentCommand {
    
    /**
     * ID của patient đặt lịch
     */
    Long patientId;
    
    /**
     * ID của schedule được chọn
     */
    Long scheduleId;
    
    /**
     * Thời gian hẹn cụ thể (phải trong khoảng của schedule)
     */
    LocalDateTime appointmentTime;
    
    /**
     * Ghi chú từ patient
     */
    String patientNotes;
    
    /**
     * Trạng thái ban đầu (mặc định là PENDING)
     */
    @Builder.Default
    AppointmentStatus status = AppointmentStatus.PENDING;
}