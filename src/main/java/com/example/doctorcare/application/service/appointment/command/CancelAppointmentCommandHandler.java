package com.example.doctorcare.application.service.appointment.command;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.doctorcare.core.enums.AppointmentStatus;
import com.example.doctorcare.infrastructure.exception.AppException;
import com.example.doctorcare.infrastructure.exception.ErrorCode;
import com.example.doctorcare.domain.business.appointment.Appointment;
import com.example.doctorcare.infrastructure.persistence.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Handler cho CancelAppointmentCommand
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CancelAppointmentCommandHandler {

    private final AppointmentRepository appointmentRepository;

    @Transactional
    public void handle(CancelAppointmentCommand command) {
        log.info("Cancelling appointment with ID: {} by status: {}", command.getAppointmentId(),
                command.getCancelledStatus());

        // 1. Find existing appointment
        Appointment appointment =
                appointmentRepository.findByIdAndDeleteFalse(command.getAppointmentId())
                        .orElseThrow(() -> new AppException(ErrorCode.APPOINTMENT_NOT_FOUND));

        // 2. Validate business rules for cancellation
        validateCancellationBusinessRules(appointment, command);

        // 3. Update appointment with cancellation details
        updateAppointmentForCancellation(appointment, command);

        // 4. Save cancelled appointment
        appointmentRepository.save(appointment);

        log.info("Successfully cancelled appointment with ID: {}", command.getAppointmentId());
    }

    /**
     * Validate business rules for appointment cancellation
     */
    private void validateCancellationBusinessRules(Appointment appointment,
            CancelAppointmentCommand command) {
        // Cannot cancel already cancelled or completed appointments
        if (appointment.getStatus() == AppointmentStatus.CANCELLED_BY_DOCTOR
                || appointment.getStatus() == AppointmentStatus.CANCELLED_BY_PATIENT
                || appointment.getStatus() == AppointmentStatus.COMPLETED) {
            throw new AppException(ErrorCode.APPOINTMENT_ALREADY_FINAL_STATUS);
        }

        // Validate cancellation status is valid
        if (command.getCancelledStatus() != AppointmentStatus.CANCELLED_BY_DOCTOR
                && command.getCancelledStatus() != AppointmentStatus.CANCELLED_BY_PATIENT) {
            throw new AppException(ErrorCode.INVALID_CANCELLATION_STATUS);
        }

        // Business rule: Can only cancel appointments that are at least X hours in advance
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime appointmentTime = appointment.getAppointmentTime();

        if (appointmentTime != null && appointmentTime.isBefore(now.plusHours(2))) {
            // Chỉ cho phép hủy nếu còn ít nhất 2 giờ
            throw new AppException(ErrorCode.CANNOT_CANCEL_APPOINTMENT_TOO_LATE);
        }

        // Additional business rule: Doctor can cancel anytime, but patient has restrictions
        if (command.getCancelledStatus() == AppointmentStatus.CANCELLED_BY_PATIENT) {
            // Có thể thêm logic kiểm tra thêm cho patient cancellation
            // Ví dụ: không cho cancel quá nhiều lần trong tháng
        }
    }

    /**
     * Update appointment with cancellation details
     */
    private void updateAppointmentForCancellation(Appointment appointment,
            CancelAppointmentCommand command) {
        // Set cancellation status
        appointment.setStatus(command.getCancelledStatus());

        // Set cancellation reason
        appointment.setCancellationReason(command.getCancellationReason());

        // Set cancellation timestamp
        appointment.setCancelledAt(LocalDateTime.now());

        // Note: cancelled_by audit field will be handled by JPA auditing
    }
}
