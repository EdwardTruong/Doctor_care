package com.example.doctorcare.model.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "doctor_user")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DoctorEntity extends BaseEntity {

	@Column(name = "description")
	String description;

	@Column(name = "achievement")
	String achievement;

	@Column(name = "training_process")
	String trainingProcess;

	@JsonIgnoreProperties("doctorEntity")
	// @JsonManagedReference
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "user_id")
	UserEntity user;

	@ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name = "doctors_specializations", joinColumns = @JoinColumn(name = "doctor_id"), inverseJoinColumns = @JoinColumn(name = "specialization_id"))
	Set<Specializations> specializations;

	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "clinic_id")
	Clinics clinic;

	@JsonIgnore // For now i don't want show all schedules of doctor.
	@OneToMany(mappedBy = "doctorEntity")
	List<Schedule> listSchedule;

	@ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JsonBackReference
	@JoinTable(name = "patients", joinColumns = @JoinColumn(name = "doctor_id"), inverseJoinColumns = @JoinColumn(name = "status_id"))
	List<Patients> listPatients;

	public void addSchedule(Schedule schedule) {
		if (this.listSchedule == null) {
			listSchedule = new ArrayList<>();
		}
		listSchedule.add(schedule);
		schedule.setDoctorEntity(this);
	}

}
