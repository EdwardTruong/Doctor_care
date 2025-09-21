package com.example.doctorcare.domain.business.specializations;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.doctorcare.domain.business.doctorsSpecializations.DoctorsSpecializations;
import com.example.doctorcare.domain.business.schedule.Schedule;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "specializations")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Specializations {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "image")
    String image;

    @Column(name = "view")
    int view;

    @Column(name = "create_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date createAt;

    @Column(name = "update_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date updateAt;

    @Column(name = "delete_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date deleteAt;

    // tạm thời tắt luôn
    @OneToMany(mappedBy = "specialization")
    List<Schedule> schedule;

    // tạm thời tắt để có thể chạy được hay không !
    @ManyToMany
    @JoinTable(name = "doctors_specializations", 
                joinColumns = @JoinColumn(name = "specialization_id"), 
                inverseJoinColumns = @JoinColumn(name = "doctor_id"))
    Set<DoctorsSpecializations> docSpecializations;

}
/*
 * -- chức vụ : n-n doctor
 * CREATE TABLE `specializations` (
 * `id` INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
 * `name` VARCHAR(255),
 * `description` TEXT,
 * `image` VARCHAR(255),
 * `createAt` DATETIME,
 * `updateAt` DATETIME,
 * `deleteAt` DATETIME
 * ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;
 */
