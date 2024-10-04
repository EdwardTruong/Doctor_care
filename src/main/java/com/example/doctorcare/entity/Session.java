package com.example.doctorcare.entity;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Table(name = "session")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Session {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id")
	int id;
	
    @Column(name="s_key")
	String key;

	@Column(name = "expires")
	@Temporal(value = TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime expires;		

	@Column(name = "data")
	String data;

	@Column(name = "create_at")
	@Temporal(value = TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime createAt;

	@Column(name = "update_at")
	@Temporal(value = TemporalType.TIMESTAMP)
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	LocalDateTime updateAt;




}
/*
 * 
 * -- phiên CREATE TABLE `session`( `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY
 * KEY, `sid` VARCHAR(36), `expires` DATETIME, `data` TEXT, `createAt` DATETIME,
 * `updateAt` DATETIME )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
 * 
 * 
 */
