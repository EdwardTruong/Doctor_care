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
 * Command để cập nhật appointment
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateAppointmentCommand {
    
    /**
     * ID của appointment cần update
     */
    Long appointmentId;
    
    /**
     * Thời gian hẹn mới (optional)
     */
    LocalDateTime appointmentTime;
    
    /**
     * Trạng thái mới (optional)
     */
    AppointmentStatus status;
    
    /**
     * Ghi chú từ patient (optional)
     */
    String patientNotes;
    
    /**
     * Ghi chú từ doctor (optional)
     */
    String doctorNotes;
    
    /**
     * Kết quả khám bệnh (optional)
     */
    String examinationResult;
}