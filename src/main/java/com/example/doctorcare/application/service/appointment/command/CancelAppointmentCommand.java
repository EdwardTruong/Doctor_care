package com.example.doctorcare.application.service.appointment.command;

import com.example.doctorcare.core.enums.AppointmentStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Command để hủy appointment
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CancelAppointmentCommand {
    
    /**
     * ID của appointment cần hủy
     */
    Long appointmentId;
    
    /**
     * Lý do hủy
     */
    String cancellationReason;
    
    /**
     * Ai hủy (CANCELLED_BY_DOCTOR hoặc CANCELLED_BY_PATIENT)
     */
    AppointmentStatus cancelledStatus;
    
    /**
     * User ID của người hủy (để audit)
     */
    Long cancelledByUserId;
}