package com.example.doctorcare.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.doctorcare.model.entity.Clinics;

@Repository
public interface ClinicsRepository extends JpaRepository<Clinics, Integer> {
	Optional<Clinics> findById(Integer id);

	Optional<Clinics> findByName(String name);

	@Query("SELECT c FROM Clinics c "
			+ "JOIN	DoctorEntity d ON d.clinic.id = c.id "
			+ "JOIN Patients p ON p.doctor.id = d.id "
			+ "GROUP BY c ORDER BY COUNT(p.id) DESC , c.view  DESC LIMIT 3")
	List<Clinics> topClinics();

	List<Clinics> findTop3ByOrderByPatients_IdCountDescAndViewDesc();

}
