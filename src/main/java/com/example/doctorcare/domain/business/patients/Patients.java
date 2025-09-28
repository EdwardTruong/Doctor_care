package com.example.doctorcare.domain.business.patients;

import java.time.LocalDate;

import com.example.doctorcare.domain.business.doctor.model.Doctor;
import com.example.doctorcare.domain.business.status_schedule.Statuses;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Table(name = "patients")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Patients {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	@Column(name = "time")
	String time;

	@Column(name = "system_note")
	String note;

	@Column(name = "active")
	int active;
	
	@Column(name="price")
	Integer price;
	
	@Column(name = "date")
	LocalDate date;
	

	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "doctor_id")
	@JsonIgnore // dùng để không hiển thị lúc load toàn bộ bệnh nhân.
	Doctor doctor;

	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "status_id")
	@JsonManagedReference
	Statuses status;
}

