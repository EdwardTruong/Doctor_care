package com.example.doctorcare.domain.business.status_schedule;

import java.util.List;

import com.example.doctorcare.core.domain.BaseEntity;
import com.example.doctorcare.domain.system.user.User;
import com.example.doctorcare.model.entity.Patients;
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
public class Statuses extends BaseEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "name")
    String name;

    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JsonIgnore
    @JoinTable(name = "patients", joinColumns = @JoinColumn(name = "status_id"), inverseJoinColumns = @JoinColumn(name = "doctor_id"))
    List<Patients> listPatients;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference // Để user load lên thông tin và những bệnh đã khám
    User user;
}


