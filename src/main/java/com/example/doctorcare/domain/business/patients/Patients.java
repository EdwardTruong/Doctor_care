package com.example.doctorcare.domain.business.patients;

import java.util.List;

import com.example.doctorcare.core.domain.BaseEntity;
import com.example.doctorcare.core.enums.AppointmentStatus;
import com.example.doctorcare.domain.business.appointment.Appointment;
import com.example.doctorcare.domain.system.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * Patient entity - Đại diện cho bệnh nhân trong hệ thống
 * Một User trở thành Patient khi đặt lịch khám với bác sỹ
 */
@Table(name = "patients")
@Entity
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Patients extends BaseEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // Reference tới User (không duplicate data)
    @ManyToOne(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    User user;

    // 4 trường Patient-specific data
    
    /**
     * Tên người đi khám - có thể khác với User nếu đăng ký hộ
     */
    @Column(name = "patient_name", nullable = false)
    String patientName;

    /**
     * Nội dung khám trước khi khám
     */
    @Column(name = "pre_exam_content", columnDefinition = "TEXT")
    String preExamContent;

    /**
     * Chi tiết bệnh sau khi khám
     */
    @Column(name = "post_exam_details", columnDefinition = "TEXT")
    String postExamDetails;

    /**
     * Trạng thái cuộc hẹn
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    @Builder.Default
    AppointmentStatus status = AppointmentStatus.PENDING;

    // Relationships
    
    /**
     * Danh sách các cuộc hẹn của bệnh nhân
     */
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    List<Appointment> appointments;
}

