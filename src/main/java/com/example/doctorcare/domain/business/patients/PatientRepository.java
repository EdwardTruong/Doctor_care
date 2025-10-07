package com.example.doctorcare.domain.business.patients;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.doctorcare.core.domain.BaseRepository;
import com.example.doctorcare.core.enums.AppointmentStatus;

@Repository
public interface PatientRepository extends BaseRepository<Patients, Long> {
    
    // Basic CRUD methods
    
    /**
     * Tìm patient theo user ID
     */
    Optional<Patients> findByUserIdAndDeletedFalse(Long userId);
    
    /**
     * Tìm tất cả patients chưa bị xóa
     */
    Page<Patients> findByDeletedFalse(Pageable pageable);
    
    /**
     * Tìm patients theo status
     */
    Page<Patients> findByStatusAndDeletedFalse(AppointmentStatus status, Pageable pageable);
    
    // Pagination methods theo yêu cầu
    
    /**
     * Phân trang patients theo Doctor ID
     */
    @Query("SELECT DISTINCT p FROM Patients p " +
           "JOIN p.appointments a " +
           "JOIN a.schedule s " +
           "WHERE s.doctorEntity.id = :doctorId " +
           "AND p.deleted = false")
    Page<Patients> findByDoctorId(@Param("doctorId") Long doctorId, Pageable pageable);
    
    /**
     * Phân trang patients theo Clinic ID
     */
    @Query("SELECT DISTINCT p FROM Patients p " +
           "JOIN p.appointments a " +
           "JOIN a.schedule s " +
           "JOIN s.doctorEntity d " +
           "WHERE d.clinic.id = :clinicId " +
           "AND p.deleted = false")
    Page<Patients> findByClinicId(@Param("clinicId") Long clinicId, Pageable pageable);
    
    /**
     * Phân trang patients theo cả Doctor ID và Clinic ID
     */
    @Query("SELECT DISTINCT p FROM Patients p " +
           "JOIN p.appointments a " +
           "JOIN a.schedule s " +
           "JOIN s.doctorEntity d " +
           "WHERE s.doctorEntity.id = :doctorId " +
           "AND d.clinic.id = :clinicId " +
           "AND p.deleted = false")
    Page<Patients> findByDoctorIdAndClinicId(
        @Param("doctorId") Long doctorId, 
        @Param("clinicId") Long clinicId, 
        Pageable pageable);
    
    // Manager dashboard queries
    
    /**
     * Đếm số lượng patients của một doctor
     */
    @Query("SELECT COUNT(DISTINCT p) FROM Patients p " +
           "JOIN p.appointments a " +
           "JOIN a.schedule s " +
           "WHERE s.doctorEntity.id = :doctorId " +
           "AND p.deleted = false")
    Long countPatientsByDoctorId(@Param("doctorId") Long doctorId);
    
    /**
     * Đếm số lượng patients theo status của một doctor
     */
    @Query("SELECT COUNT(DISTINCT p) FROM Patients p " +
           "JOIN p.appointments a " +
           "JOIN a.schedule s " +
           "WHERE s.doctorEntity.id = :doctorId " +
           "AND p.status = :status " +
           "AND p.deleted = false")
    Long countPatientsByDoctorIdAndStatus(
        @Param("doctorId") Long doctorId, 
        @Param("status") AppointmentStatus status);
    
    /**
     * Tìm patients theo doctor với status cụ thể
     */
    @Query("SELECT DISTINCT p FROM Patients p " +
           "JOIN p.appointments a " +
           "JOIN a.schedule s " +
           "WHERE s.doctorEntity.id = :doctorId " +
           "AND p.status = :status " +
           "AND p.deleted = false")
    Page<Patients> findByDoctorIdAndStatus(
        @Param("doctorId") Long doctorId, 
        @Param("status") AppointmentStatus status, 
        Pageable pageable);
    
    /**
     * Tìm patients theo tên (search)
     */
    @Query("SELECT p FROM Patients p " +
           "WHERE LOWER(p.patientName) LIKE LOWER(CONCAT('%', :name, '%')) " +
           "AND p.deleted = false")
    Page<Patients> findByPatientNameContainingIgnoreCase(
        @Param("name") String name, 
        Pageable pageable);
}
