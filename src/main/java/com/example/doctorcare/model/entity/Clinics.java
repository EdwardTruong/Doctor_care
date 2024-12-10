package com.example.doctorcare.model.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.doctorcare.common.utils.Const.MESSENGER_FIELDS_ERROR;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Table(name = "clinics")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Clinics extends BaseEntity {

	@NotBlank(message = MESSENGER_FIELDS_ERROR.NAME_ERROR)
	@Size(min = 6, max = 40)
	@Column(name = "name")
	String name;

	@Pattern(regexp = "^\\d{3,12}", message = MESSENGER_FIELDS_ERROR.PHONE_ERROR)
	@Column(name = "phone")
	String phone;

	@Column(name = "address")
	@NotBlank(message = MESSENGER_FIELDS_ERROR.ADDRESS_ERROR)
	String address;

	@Column(name = "view")
	int view;

	@Column(name = "introduction_HTML")
	@NotBlank(message = MESSENGER_FIELDS_ERROR.HTML_INTRO)
	String introductionHTML;

	@Column(name = "introduction_markdown")
	@NotBlank(message = MESSENGER_FIELDS_ERROR.MARDOWN_INTRO)
	String introductionMarkdown;

	@Column(name = "description")
	String description;

	@Column(name = "image")
	String image;

	@OneToMany(mappedBy = "clinic", cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JsonBackReference
	List<DoctorEntity> listDoctorEntity;

	@JsonManagedReference
	@ManyToOne()
	@JoinColumn(name = "place_id")
	Places place;

	public void addDoctor(DoctorEntity doctor) {
		if (listDoctorEntity == null) {
			listDoctorEntity = new ArrayList<>();
		}

		this.listDoctorEntity.add(doctor);
		doctor.setClinic(this);

	}
}
/*
 * -- phòng khám (1-1 doctor_user)
 * CREATE TABLE `clinics`(
 * `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
 * `name` VARCHAR(225),
 * `address` VARCHAR(225),
 * `phone` VARCHAR(255) ,
 * `introductionHTML` TEXT,
 * `introductionMarkdown`TEXT,
 * `description`TEXT,
 * `image` VARCHAR(225),
 * `createAt` DATETIME
 * )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
 */
