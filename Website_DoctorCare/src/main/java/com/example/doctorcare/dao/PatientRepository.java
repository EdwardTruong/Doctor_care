package com.example.doctorcare.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.doctorcare.entity.Patients;

@Repository
public interface PatientRepository extends JpaRepository<Patients, Integer> {
	
	@Query("SELECT p FROM Patients p WHERE p.doctor.id = :idDoc AND p.date = :inputDate")
	List<Patients> listPatientsOfDoctor(@Param("idDoc") Integer idDoctor, @Param("inputDate") LocalDate date);
	
	
	@Query("SELECT p FROM Patients p "
			+ "JOIN Statuses s 		ON s.id = p.status.id "
			+ "JOIN UserEntity u 	ON u.id = s.user.id "
			+ "WHERE u.id = :idUser")
	List<Patients> listPatientsByUserId(@Param("idUser") Integer idUser);

	
	@Query("SELECT p FROM Patients p "
			+"JOIN Statuses s 		ON s.id = p.status.id "
			+"JOIN UserEntity u 	ON u.id = s.user.id "
			+"WHERE u.id = :idUser AND p.date = :date AND p.time = :time"
			)
	Patients getPatientsByUserIdAndDate(@Param("idUser") Integer idUser, @Param("date") LocalDate date,@Param("time") String time);
}
