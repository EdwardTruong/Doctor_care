package com.example.doctorcare.infrastructure.persistence.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.doctorcare.core.domain.BaseRepository;
import com.example.doctorcare.core.enums.AppointmentStatus;
import com.example.doctorcare.domain.business.appointment.Appointment;

/**
 * Repository cho Appointment entity với các truy vấn phức tạp
 */
@Repository
public interface AppointmentRepository extends BaseRepository<Appointment, Long> {

       Optional<Appointment> findByIdAndDeleteFalse(Long id);

       /**
        * Tìm appointments theo patient ID với phân trang
        */
       @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.deleted = false")
       Page<Appointment> findByPatientId(@Param("patientId") Long patientId, Pageable pageable);

       /**
        * Tìm appointments theo doctor ID với phân trang
        */
       @Query("SELECT a FROM Appointment a WHERE a.schedule.doctor.id = :doctorId AND a.deleted = false")
       Page<Appointment> findByDoctorId(@Param("doctorId") Long doctorId, Pageable pageable);

       /**
        * Tìm appointments theo clinic ID với phân trang
        */
       @Query("SELECT a FROM Appointment a WHERE a.schedule.clinic.id = :clinicId AND a.deleted = false")
       Page<Appointment> findByClinicId(@Param("clinicId") Long clinicId, Pageable pageable);

       /**
        * Tìm appointments theo doctor ID và clinic ID
        */
       @Query("SELECT a FROM Appointment a WHERE a.schedule.doctor.id = :doctorId "
                     + "AND a.schedule.clinic.id = :clinicId AND a.deleted = false")
       Page<Appointment> findByDoctorIdAndClinicId(@Param("doctorId") Long doctorId,
                     @Param("clinicId") Long clinicId, Pageable pageable);

       /**
        * Tìm appointments theo trạng thái
        */
       @Query("SELECT a FROM Appointment a WHERE a.status = :status AND a.deleted = false")
       Page<Appointment> findByStatus(@Param("status") AppointmentStatus status, Pageable pageable);

       /**
        * Tìm appointments theo doctor và trạng thái
        */
       @Query("SELECT a FROM Appointment a WHERE a.schedule.doctor.id = :doctorId "
                     + "AND a.status = :status AND a.deleted = false")
       Page<Appointment> findByDoctorIdAndStatus(@Param("doctorId") Long doctorId,
                     @Param("status") AppointmentStatus status, Pageable pageable);

       /**
        * Tìm appointments trong khoảng thời gian
        */
       @Query("SELECT a FROM Appointment a WHERE a.appointmentTime BETWEEN :startTime AND :endTime "
                     + "AND a.deleted = false")
       Page<Appointment> findByAppointmentTimeBetween(@Param("startTime") LocalDateTime startTime,
                     @Param("endTime") LocalDateTime endTime, Pageable pageable);

       /**
        * Tìm appointments của doctor trong ngày cụ thể
        */
       @Query("SELECT a FROM Appointment a WHERE a.schedule.doctor.id = :doctorId "
                     + "AND DATE(a.appointmentTime) = :date AND a.deleted = false")
       Page<Appointment> findByDoctorIdAndDate(@Param("doctorId") Long doctorId,
                     @Param("date") LocalDate date, Pageable pageable);

       /**
        * Tìm appointments của patient trong khoảng thời gian
        */
       @Query("SELECT a FROM Appointment a WHERE a.patient.id = :patientId "
                     + "AND a.appointmentTime BETWEEN :startTime AND :endTime AND a.deleted = false")
       Page<Appointment> findByPatientIdAndTimeBetween(@Param("patientId") Long patientId,
                     @Param("startTime") LocalDateTime startTime,
                     @Param("endTime") LocalDateTime endTime, Pageable pageable);

       /**
        * Truy vấn phức hợp với nhiều filters
        */
       @Query("SELECT a FROM Appointment a WHERE "
                     + "(:patientId IS NULL OR a.patient.id = :patientId) AND "
                     + "(:doctorId IS NULL OR a.schedule.doctor.id = :doctorId) AND "
                     + "(:clinicId IS NULL OR a.schedule.clinic.id = :clinicId) AND "
                     + "(:status IS NULL OR a.status = :status) AND "
                     + "(:startTime IS NULL OR a.appointmentTime >= :startTime) AND "
                     + "(:endTime IS NULL OR a.appointmentTime <= :endTime) AND "
                     + "a.deleted = false")
       Page<Appointment> findAppointmentsWithFilters(@Param("patientId") Long patientId,
                     @Param("doctorId") Long doctorId, @Param("clinicId") Long clinicId,
                     @Param("status") AppointmentStatus status,
                     @Param("startTime") LocalDateTime startTime,
                     @Param("endTime") LocalDateTime endTime, Pageable pageable);

       /**
        * Kiểm tra conflict appointment (cùng patient, cùng thời gian)
        */
       @Query("SELECT COUNT(a) FROM Appointment a WHERE a.patient.id = :patientId "
                     + "AND a.appointmentTime = :appointmentTime "
                     + "AND a.status IN ('PENDING', 'CONFIRMED') " + "AND a.deleted = false "
                     + "AND (:excludeId IS NULL OR a.id != :excludeId)")
       long countConflictingAppointments(@Param("patientId") Long patientId,
                     @Param("appointmentTime") LocalDateTime appointmentTime,
                     @Param("excludeId") Long excludeId);

       /**
        * Kiểm tra schedule availability (số appointment trong schedule)
        */
       @Query("SELECT COUNT(a) FROM Appointment a WHERE a.schedule.id = :scheduleId "
                     + "AND a.status IN ('PENDING', 'CONFIRMED') " + "AND a.deleted = false")
       long countActiveAppointmentsBySchedule(@Param("scheduleId") Long scheduleId);

       /**
        * Dashboard queries - Đếm appointments theo trạng thái của doctor
        */
       @Query("SELECT COUNT(a) FROM Appointment a WHERE a.schedule.doctor.id = :doctorId "
                     + "AND a.status = :status AND a.deleted = false")
       long countByDoctorIdAndStatus(@Param("doctorId") Long doctorId,
                     @Param("status") AppointmentStatus status);

       /**
        * Dashboard queries - Đếm appointments hôm nay của doctor
        */
       @Query("SELECT COUNT(a) FROM Appointment a WHERE a.schedule.doctor.id = :doctorId "
                     + "AND DATE(a.appointmentTime) = CURRENT_DATE "
                     + "AND a.status IN ('PENDING', 'CONFIRMED') " + "AND a.deleted = false")
       long countTodayAppointmentsByDoctor(@Param("doctorId") Long doctorId);

       /**
        * Dashboard queries - Đếm appointments tuần này của doctor
        */
       @Query("SELECT COUNT(a) FROM Appointment a WHERE a.schedule.doctor.id = :doctorId "
                     + "AND WEEK(a.appointmentTime) = WEEK(CURRENT_DATE) "
                     + "AND YEAR(a.appointmentTime) = YEAR(CURRENT_DATE) "
                     + "AND a.deleted = false")
       long countThisWeekAppointmentsByDoctor(@Param("doctorId") Long doctorId);

       /**
        * Dashboard queries - Tổng appointments của clinic
        */
       @Query("SELECT COUNT(a) FROM Appointment a WHERE a.schedule.clinic.id = :clinicId "
                     + "AND a.deleted = false")
       long countByClinicId(@Param("clinicId") Long clinicId);

       /**
        * Find all active appointments (not deleted)
        */
       @Query("SELECT a FROM Appointment a WHERE a.deleted = false")
       Page<Appointment> findAllActive(Pageable pageable);
}
