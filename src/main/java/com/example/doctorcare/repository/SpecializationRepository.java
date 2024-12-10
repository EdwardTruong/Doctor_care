package com.example.doctorcare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.doctorcare.model.entity.Specializations;

@Repository
public interface SpecializationRepository extends JpaRepository<Specializations, Integer> {
	Optional<Specializations> findById(Integer id);
	Optional<Specializations> findByName(String name);

	
	@Query("SELECT s FROM Specializations s "
			+ "JOIN DoctorsSpecializations ds 	ON ds.specializations.id = s.id "
			+ "JOIN	DoctorEntity d 				ON ds.doctor.id = d.id "
			+ "JOIN Patients p 					ON p.doctor.id = d.id "
			+ "GROUP BY s ORDER BY COUNT(p.id) DESC , s.view  DESC LIMIT 3")
	List<Specializations> topSpecializations();

	
	
	
	/*
	 * For testing
	 */
	@Query("SELECT s.name FROM Specializations s "
			+ "JOIN DoctorsSpecializations ds ON ds.specializations.id = s.id "
			+ "JOIN Schedule sch ON sch.specialization.id = ds.specializations.id "
			+ "JOIN DoctorEntity d ON d.id = sch.doctorEntity.id "
			+ "JOIN Patients p ON p.doctor.id = d.id "
			+ "WHERE p.id = :patientId GROUP BY (s.name)")
	Optional<String> findNameOfSpecialization(@Param("patientId") Integer idPatient);
	
	


}
