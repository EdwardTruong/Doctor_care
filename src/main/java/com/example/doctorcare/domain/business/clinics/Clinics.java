package com.example.doctorcare.domain.business.clinics;

import java.util.ArrayList;
import java.util.List;

import com.example.doctorcare.core.domain.BaseEntity;
import com.example.doctorcare.domain.business.doctor.model.Doctor;
import com.example.doctorcare.domain.business.places.Places;
import com.example.doctorcare.domain.system.user.User;
import com.example.doctorcare.infrastructure.common.utils.Const.MESSENGER_FIELDS_ERROR;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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

@Table(name="clinics")
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Clinics extends BaseEntity<Long> {

	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@NotBlank(message = MESSENGER_FIELDS_ERROR.NAME_ERROR)
	@Size(min = 6, max = 40)
    @Column(name="name")
    String name;
	
	@Pattern(regexp = "^\\d{3,12}", message = MESSENGER_FIELDS_ERROR.PHONE_ERROR)
	@Column(name="phone")
    String phone;
        
	@Column(name="address")
	@NotBlank(message  = MESSENGER_FIELDS_ERROR.ADDRESS_ERROR)
    String address;
    
	@Column(name="view")
	int view;
	
    @Column(name="introduction_HTML")
	@NotBlank(message = MESSENGER_FIELDS_ERROR.HTML_INTRO)
    String introductionHTML;
    
    @Column(name="introduction_markdown")
	@NotBlank(message = MESSENGER_FIELDS_ERROR.MARDOWN_INTRO)
    String introductionMarkdown;
    
    @Column(name="description")
    String description;
    
    @Column(name="image")
    String imageUrl;


	@OneToMany(mappedBy = "clinic" , cascade = {CascadeType.DETACH,CascadeType.MERGE,CascadeType.PERSIST,CascadeType.REFRESH})
	@JsonBackReference
	List<Doctor> listDoctor;
	
	@JsonManagedReference
	@ManyToOne()
	@JoinColumn(name="place_id")
	Places place;
	
	@ManyToOne()
	@JoinColumn(name = "user_id") 
	User owner;

	public void addDoctor(Doctor doctor) {
		if(listDoctor == null) {
			listDoctor =  new ArrayList<>();
		}
		this.listDoctor.add(doctor);
		doctor.setClinic(this);
		
	}
}
/*    -- phòng khám (1-1 doctor_user)
      CREATE TABLE `clinics`(
  	`id` INT(11)  NOT NULL AUTO_INCREMENT PRIMARY KEY,
	`name` VARCHAR(225),
    `address` VARCHAR(225),
    `phone` VARCHAR(255)  ,
    `introductionHTML` TEXT,
    `introductionMarkdown`TEXT,
    `description`TEXT,
    `image` VARCHAR(225),
	`createAt` DATETIME
    )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;   */
