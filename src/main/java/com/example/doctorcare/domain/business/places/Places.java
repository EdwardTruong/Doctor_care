package com.example.doctorcare.domain.business.places;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.doctorcare.core.domain.BaseEntity;
import com.example.doctorcare.domain.business.clinics_.Clinics;
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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name="places")
@Getter
@Setter
@Builder
public class Places extends BaseEntity<Long>{

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(name = "name")
	String name;

	@Column(name = "delete_at")
	@Temporal(value = TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime deleteAt;
	
	@JsonBackReference
	@OneToMany(mappedBy = "place")
	List<Clinics> clinic;

	Places parent;
}
/*
 * CREATE TABLE `places`( `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
 * `name` VARCHAR(255), 
 * `create_at` DATETIME,
 *  `update_at` DATETIME, 
 *  `delete_at`DATETIME DEFAULT NULL 
 * 
 * )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ;
 * 
 * 
 * 
 */