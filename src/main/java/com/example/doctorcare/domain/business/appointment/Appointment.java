package com.example.doctorcare.domain.business.appointment;

import java.time.LocalDateTime;
import com.example.doctorcare.core.domain.BaseEntity;
import com.example.doctorcare.core.enums.AppointmentStatus;
import com.example.doctorcare.domain.business.patients.Patients;
import com.example.doctorcare.domain.business.schedule.Schedule;
import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Entity Appointment - Kết nối Patient với Schedule
 * Đại diện cho một cuộc hẹn khám bệnh cụ thể
 * 
 * Business Logic:
 * - Patient đặt lịch với Doctor tại Clinic vào thời gian cụ thể (Schedule)
 * - Mỗi appointment có trạng thái riêng (PENDING, CONFIRMED, CANCELLED, COMPLETED)
 * - Có thể có ghi chú từ bệnh nhân và kết quả từ bác sĩ
 */
@Entity
@Table(name = "appointments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Appointment extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    /**
     * Bệnh nhân đặt lịch
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    @JsonBackReference("patient-appointments")
    Patients patient;

    /**
     * Lịch làm việc của bác sĩ (bao gồm Doctor, Clinic, thời gian)
     */
    @NotNull
    @ManyToOne
    @JoinColumn(name = "schedule_id", nullable = false)
    Schedule schedule;

    /**
     * Trạng thái cuộc hẹn
     */
    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    AppointmentStatus status;

    /**
     * Thời gian hẹn cụ thể (trong khoảng thời gian của Schedule)
     */
    @Column(name = "appointment_time")
    LocalDateTime appointmentTime;

    /**
     * Ghi chú từ bệnh nhân khi đặt lịch
     */
    @Column(name = "patient_notes", columnDefinition = "TEXT")
    String patientNotes;

    /**
     * Ghi chú từ bác sĩ (sau khi khám)
     */
    @Column(name = "doctor_notes", columnDefinition = "TEXT")
    String doctorNotes;

    /**
     * Kết quả khám bệnh
     */
    @Column(name = "examination_result", columnDefinition = "TEXT")
    String examinationResult;

    /**
     * Lý do hủy (nếu có)
     */
    @Column(name = "cancellation_reason")
    String cancellationReason;

    /**
     * Thời gian hủy
     */
    @Column(name = "cancelled_at")
    LocalDateTime cancelledAt;

    /**
     * Thời gian hoàn thành khám bệnh
     */
    @Column(name = "completed_at")
    LocalDateTime completedAt;
}


