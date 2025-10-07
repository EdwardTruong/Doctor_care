package com.example.doctorcare.application.service.appointment.query;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Query để lấy chi tiết một appointment
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GetAppointmentDetailQuery {
    
    /**
     * ID của appointment cần lấy chi tiết
     */
    Long appointmentId;
}