package com.example.doctorcare.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.doctorcare.model.dto.DoctorDto;
import com.example.doctorcare.model.entity.DoctorEntity;

@Repository
public interface DoctorRepository extends JpaRepository<DoctorEntity, Integer> {
	Optional<DoctorEntity> findById(Integer id);
	
	
    //Find the doctor(s) with schedule :
	@Query("SELECT new com.example.doctorcare.dto.DoctorDto(u.name, d.achievement, sch ) "
		    + "FROM DoctorEntity d "
		    + "JOIN UserEntity u 				ON u.id = d.user.id "
		    + "JOIN DoctorsSpecializations ds 	ON ds.doctor.id = d.id "
		    + "JOIN Specializations s 			ON s.id = ds.specializations.id "
		    + "JOIN Schedule sch	 			ON sch.doctorEntity.id = d.id "
			+ "WHERE s.name = :inputName AND sch.date = :inputDate")
		List<DoctorDto> listDoctorSpecializationWithSchedule(@Param("inputName") String name, @Param("inputDate") LocalDate date);

	
	/*
	 * SELECT u.name AS doctor_name, s.name AS specialization_name
		FROM doctor_user du
		JOIN User u 						ON du.user_id = u.id
		JOIN doctors_specializations ds 	ON du.id = ds.doctor_id
		JOIN specializations s 				ON ds.specialization_id = s.id
		JOIN schedules sch 					ON du.id = sch.doctor_id
		WHERE s.name = 'nhập vào_specialization_name' AND sch.date = 'nhập vào_date';
	 */
}
