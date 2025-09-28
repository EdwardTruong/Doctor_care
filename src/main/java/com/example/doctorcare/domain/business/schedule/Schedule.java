package com.example.doctorcare.domain.business.schedule;


import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.doctorcare.domain.business.doctor.model.Doctor;
import com.example.doctorcare.domain.business.specializations.Specializations;
import com.fasterxml.jackson.annotation.JsonIgnore;

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

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedules")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Schedule {

	@JsonIgnore
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	@Column(name = "date")
	LocalDate date;

	@Column(name = "time")
	String time;

	@Column(name = "max_Booking")
	String maxBooking;

	@Column(name="price")
	Integer price;
	
	@Column(name = "sum_Booking")
	Integer sumBooking;

	@Column(name = "create_at")
	LocalDateTime createAt;

	@Column(name = "update_at")
	LocalDateTime updateAt;

	@Column(name = "delete_at")
	LocalDateTime deleteAt;

		// @JsonManagedReference // if doctor entity load all schedules i need add this
	// to json don't call back doctorEntity and show it infinity
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "doctor_id")
	@JsonIgnore
	Doctor doctorEntity;
	
// tạm thời tắt để xem chạy có được hay không !	
	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "specialization_id")
	Specializations specialization;


}
