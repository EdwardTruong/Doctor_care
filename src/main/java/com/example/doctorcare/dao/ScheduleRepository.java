package com.example.doctorcare.dao;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.doctorcare.entity.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

	// @Query("SELECT s FROM Schedule s WHERE s.doctorEntity.id :=inpuIdtDoctor AND
	// s.date :=inputDate")
	// Optional<Schedule> findScheduleByDoctor(@Param("inpuIdtDoctor")Integer
	// idDoctor, @Param("inputDate") String date);

	@Query("SELECT s FROM Schedule s WHERE s.doctorEntity.id = :idDoc AND s.date = :inputDate")
	Optional<Schedule> findScheduleByDoctor(@Param("idDoc") Integer idDoctor, @Param("inputDate") LocalDate date);

	@Query("SELECT sch FROM " +
	 		 " Schedule sch " +
	          "JOIN DoctorEntity d 				ON sch.doctorEntity.id 		= d.id " +
	 		  "JOIN DoctorsSpecializations ds 	ON ds.doctor.id 			= d.id " +
	          "JOIN Specializations s 			ON ds.specializations.id 	= s.id " +
	          "JOIN Clinics c 					ON d.clinic.id 		  		= c.id " +
	          "JOIN Places p 					ON c.place.id 				= p.id " +
	          "WHERE sch.price >= :minPrice AND sch.price <= :maxPrice "
	          +"AND CONCAT(p.name, ' ', s.name, ' ', c.name, ' ') LIKE %:searchString% AND sch.date = :date")
	List<Schedule> findScheduleTimeByPriceRangeUsingConcat(@Param("searchString") String searchString,
			@Param("date") LocalDate date, @Param("minPrice") Integer minPrice, @Param("maxPrice") Integer maxPrice);

	
	/*
	 * For testing
	 */
	@Query("SELECT sch FROM " 
			+ "Schedule sch "
			+ "JOIN DoctorEntity d 				ON sch.doctorEntity.id 		= d.id "
			+ "JOIN DoctorsSpecializations ds 	ON ds.doctor.id 			= d.id "
			+ "JOIN Specializations s 			ON ds.specializations.id 	= s.id "
			+ "JOIN Clinics c 					ON d.clinic.id 		  		= c.id "
			+ "JOIN Places p 					ON c.place.id 				= p.id " + "WHERE p.id = :placeId "
			+ "AND s.id = :sId " + "AND c.id = :cId " + "AND sch.price IN (:priceList)")
	List<Schedule> findScheduleTimeByPriceRange(@Param("placeId") Integer placeId,
			@Param("sId") Integer specializationId, @Param("cId") Integer clinicId,
			@Param("priceList") List<String> priceList);

	/*
	 * For testing
	 */
	@Query("SELECT s FROM Schedule s WHERE s.doctorEntity.id = :idDoc")
	List<Schedule> getSchedulesOfDoctorId(@Param("idDoc")Integer idDoc);
}
