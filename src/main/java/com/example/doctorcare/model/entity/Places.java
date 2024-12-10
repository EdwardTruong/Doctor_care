package com.example.doctorcare.model.entity;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
@AllArgsConstructor
@Entity
@Table(name = "places")
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Places extends BaseEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int id;

	@Column(name = "name")
	String name;

	@Column(name = "delete_at")
	@Temporal(value = TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime deleteAt;

	@JsonBackReference
	@OneToMany(mappedBy = "place")
	List<Clinics> clinic;
}
/*
 * CREATE TABLE `places`( `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
 * `name` VARCHAR(255),
 * `create_at` DATETIME,
 * `update_at` DATETIME,
 * `delete_at`DATETIME DEFAULT NULL
 * 
 * )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ;
 * 
 * 
 * 
 */
