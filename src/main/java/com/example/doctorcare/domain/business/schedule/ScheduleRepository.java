package com.example.doctorcare.domain.business.schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.doctorcare.core.domain.BaseRepository;

@Repository
public interface ScheduleRepository extends BaseRepository<Schedule, Long> {

       /**
        * Tìm schedules theo doctor ID với pagination
        */
       Page<Schedule> findByDoctorIdAndDeletedFalse(Long doctorId, Pageable pageable);

       /**
        * Tìm schedules theo doctor ID và date range
        */
       @Query("SELECT s FROM Schedule s WHERE s.doctor.id = :doctorId "
                     + "AND s.deleted = false " + "AND (:dateFrom IS NULL OR s.date >= :dateFrom) "
                     + "AND (:dateTo IS NULL OR s.date <= :dateTo)")
       Page<Schedule> findByDoctorIdWithDateRange(@Param("doctorId") Long doctorId,
                     @Param("dateFrom") LocalDate dateFrom, @Param("dateTo") LocalDate dateTo,
                     Pageable pageable);

       /**
        * Tìm schedules available (có slot trống)
        */
       @Query("SELECT s FROM Schedule s WHERE s.deleted = false "
                     + "AND CAST(s.maxBooking AS integer) > s.sumBooking")
       Page<Schedule> findAvailableSchedules(Pageable pageable);

       /**
        * Tìm schedules theo specialization
        */
       Page<Schedule> findBySpecializationIdAndDeletedFalse(Long specializationId,
                     Pageable pageable);

       /**
        * Tìm schedules theo date range
        */
       @Query("SELECT s FROM Schedule s WHERE s.deleted = false "
                     + "AND (:dateFrom IS NULL OR s.date >= :dateFrom) "
                     + "AND (:dateTo IS NULL OR s.date <= :dateTo)")
       Page<Schedule> findByDateRange(@Param("dateFrom") LocalDate dateFrom,
                     @Param("dateTo") LocalDate dateTo, Pageable pageable);

       /**
        * Tìm schedules theo price range
        */
       @Query("SELECT s FROM Schedule s WHERE s.deleted = false "
                     + "AND (:minPrice IS NULL OR s.price >= :minPrice) "
                     + "AND (:maxPrice IS NULL OR s.price <= :maxPrice)")
       Page<Schedule> findByPriceRange(@Param("minPrice") Integer minPrice,
                     @Param("maxPrice") Integer maxPrice, Pageable pageable);

       /**
        * Tìm schedules theo keyword (search trong doctor name, time, specialization)
        */
       @Query("SELECT s FROM Schedule s " + "LEFT JOIN s.doctor d " + "LEFT JOIN d.user u "
                     + "LEFT JOIN s.specialization sp " + "WHERE s.deleted = false "
                     + "AND (:keyword IS NULL OR "
                     + "     LOWER(u.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
                     + "     LOWER(u.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR "
                     + "     LOWER(sp.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
       Page<Schedule> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

       /**
        * Kiểm tra schedule conflict cho doctor trong time slot
        */
       @Query("""
                     SELECT COUNT(s)
                     FROM Schedule s
                     WHERE s.doctor.id = :doctorId
                     AND s.date = :date
                     AND s.startTime = :startTime
                     AND s.endTime = :endTime
                     AND s.deleted = false
                     AND (:excludeId IS NULL OR s.id != :excludeId)
                     """)
       long countConflictingSchedules(@Param("doctorId") Long doctorId, @Param("date") LocalDate date,
                     @Param("startTime") LocalTime startTime, @Param("endTime") LocalTime endTime,
                     @Param("excludeId") Long excludeId);

       /**
        * Lấy schedules sắp tới của doctor
        */
       @Query("SELECT s FROM Schedule s WHERE s.doctor.id = :doctorId "
                     + "AND s.date >= CURRENT_DATE AND s.deleted = false "
                     + "ORDER BY s.date ASC, s.startTime ASC")
       List<Schedule> findUpcomingSchedulesByDoctor(@Param("doctorId") Long doctorId);

       /**
        * Đếm tổng bookings của doctor trong khoảng thời gian
        */
       @Query("SELECT COALESCE(SUM(s.sumBooking), 0) FROM Schedule s "
                     + "WHERE s.doctor.id = :doctorId "
                     + "AND s.date BETWEEN :dateFrom AND :dateTo " + "AND s.deleted = false")
       Integer getTotalBookingsByDoctorInPeriod(@Param("doctorId") Long doctorId,
                     @Param("dateFrom") LocalDate dateFrom, @Param("dateTo") LocalDate dateTo);
}
