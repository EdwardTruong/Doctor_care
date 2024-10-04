package com.example.doctorcare.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/*
 * Trạng thái chính là đặc lịch với doctor
 */

@Entity
@Table(name = "statuses")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Statuses {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "create_at")
    LocalDateTime createdAt;

    @Column(name = "update_at")
    LocalDateTime updateAt;

    @Column(name = "delete_at")
    LocalDateTime deleteAt;

    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JsonIgnore
    @JoinTable(name = "patients", 
    			joinColumns = @JoinColumn(name = "status_id"),
    			inverseJoinColumns = @JoinColumn(name = "doctor_id"))
    List<Patients> listPatients;

    
    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonBackReference // Để user load lên thông tin và những bệnh đã khám
    UserEntity user;
}

/*
 * -- trạng thái (n-n-patients statuses - doctor_user )
 * CREATE TABLE `statuses`(
 * `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
 * `name` VARCHAR(255),
 * `create_at` DATETIME,
 * `update_at` DATETIME,
 * `delete_at` DATETIME DEFAULT NULL
 * )ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
 */
