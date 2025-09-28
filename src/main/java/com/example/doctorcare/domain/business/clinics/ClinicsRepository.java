package com.example.doctorcare.domain.business.clinics;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.example.doctorcare.core.domain.BaseRepository;

public interface ClinicsRepository extends BaseRepository<Clinics, Long>  {
	Optional<Clinics> findByIdAndDeletedFalse(Integer id);

	Optional<Clinics> findByNameAndDeletedFalse(String name);

	@Query("SELECT c FROM Clinics c " 
			+ "JOIN	DoctorEntity d ON d.clinic.id = c.id "
			+ "JOIN Patients p ON p.doctor.id = d.id " 
			+ "GROUP BY c ORDER BY COUNT(p.id) DESC , c.view  DESC LIMIT 3")
	List<Clinics> topClinics();
}
