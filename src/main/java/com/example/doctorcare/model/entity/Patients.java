package com.example.doctorcare.model.entity;

import java.time.LocalDate;

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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Table(name = "patients")
@Entity
@Setter
@Getter
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
	DoctorEntity doctor;

	@ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "status_id")
	@JsonManagedReference
	Statuses status;
}

/*
 * -- Người bệnh : n-n (giữa - statust - doctor) CREATE TABLE `patients`( `id`
 * INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY, `doctor_id` INT(11), `status_id`
 * INT(11), `name` VARCHAR(255), FOREIGN KEY (`doctor_id`) REFERENCES
 * `doctor_user`(`id`), FOREIGN KEY (`status_id`) REFERENCES `statuses`(`id`)
 * )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
 */