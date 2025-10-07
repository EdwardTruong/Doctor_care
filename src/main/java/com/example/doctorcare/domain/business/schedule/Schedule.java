package com.example.doctorcare.domain.business.schedule;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.example.doctorcare.core.domain.BaseEntity;
import com.example.doctorcare.core.enums.ScheduleStatus;
import com.example.doctorcare.domain.business.appointment.Appointment;
import com.example.doctorcare.domain.business.clinics_.Clinics;
import com.example.doctorcare.domain.business.doctor.Doctor;
import com.example.doctorcare.domain.business.specializations.Specializations;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Entity Schedule - Lịch làm việc của bác sĩ
 * 
 * Đại diện cho khung giờ làm việc cụ thể của bác sĩ tại phòng khám trong một ngày
 * Ví dụ: Bác sĩ A làm việc tại Clinic B từ 10:00-11:00 ngày 15/10/2024
 */
@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "schedules")
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Schedule extends BaseEntity<Long> {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	/**
	 * Ngày của schedule - không tạo được cho quá khứ
	 */
	@NotNull
	@Column(name = "date", nullable = false)
	LocalDate date;

	/**
	 * Ngày làm việc
	 */
	@NotNull
	@Column(name = "work_date", nullable = false)
	LocalDate workDate;

	/**
	 * Thời gian bắt đầu trong ngày (ví dụ: 10:00)
	 */
	@NotNull
	@Column(name = "start_time", nullable = false)
	LocalTime startTime;
	
	/**
	 * Thời gian kết thúc trong ngày (ví dụ: 11:00)
	 */
	@NotNull
	@Column(name = "end_time", nullable = false)
	LocalTime endTime;

	/**
	 * Tổng số booking thực sẽ được update, nếu mà nó có giá trị thì không được xóa.
	 */
	@Column(name="sum_booking", columnDefinition = "INTEGER DEFAULT 0")
	@Builder.Default
	Integer sumBooking = 0;


	/**
	 * Tổng số booking dự kiến khi tạo ra, dùng làm cờ để kiểm tra vượt quá giá trị đã dự kiến. 
	 */
	@Column(name="max_booking")
	Integer maxBooking;

	@NotNull
	@Column(name ="price", nullable = false)
	Double price;
	/**
	 * Bác sĩ phụ trách
	 */
	@NotNull
	@ManyToOne
	@JoinColumn(name = "doctor_id", nullable = false)
	@JsonIgnore
	Doctor doctor;

	/**
	 * Phòng khám nơi bác sĩ làm việc
	 */
	@NotNull
	@ManyToOne
	@JoinColumn(name = "clinic_id", nullable = false)
	Clinics clinic;

	/**
	 * Chuyên khoa (có thể null nếu bác sĩ đa khoa)
	 */
	@ManyToOne
	@JoinColumn(name = "specialization_id")
	Specializations specialization;

	/**
	 * Số lượng appointment tối đa có thể book trong khung giờ này
	 */
	@Column(name = "max_appointments")
	@Builder.Default
	Integer maxAppointments = 10;

	/**
	 * Giá khám cho khung giờ này (có thể khác nhau theo giờ)
	 */
	@Column(name = "consultation_fee")
	Integer consultationFee;

	/**
	 * Trạng thái của schedule
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	@Builder.Default
	ScheduleStatus status = ScheduleStatus.AVAILABLE;

	/**
	 * Ghi chú cho schedule (ví dụ: "Khám bệnh tim mạch", "Tư vấn online")
	 */
	@Column(name = "note")
	String note;

	/**
	 * Loại hình khám (ví dụ: "ONLINE", "OFFLINE", "HOME_VISIT")
	 */
	@Column(name = "consultation_type")
	@Builder.Default
	String consultationType = "OFFLINE";

	/**
	 * Danh sách appointments đã được đặt cho schedule này
	 */
	@OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL)
	@JsonManagedReference
	List<Appointment> appointments;

	/**
	 * Helper method để lấy full datetime từ workDate và startTime
	 */
	public LocalDateTime getStartDateTime() {
		return LocalDateTime.of(workDate, startTime);
	}

	/**
	 * Helper method để lấy full datetime từ workDate và endTime
	 */
	public LocalDateTime getEndDateTime() {
		return LocalDateTime.of(workDate, endTime);
	}

	/**
	 * Kiểm tra xem schedule có available không
	 */
	public boolean isAvailable() {
		return status == ScheduleStatus.AVAILABLE && 
			   getEndDateTime().isAfter(LocalDateTime.now());
	}

	/**
	 * Lấy số appointment hiện tại
	 */
	public int getCurrentAppointmentCount() {
		return appointments != null ? appointments.size() : 0;
	}

	/**
	 * Kiểm tra còn chỗ trống không
	 */
	public boolean hasAvailableSlots() {
		return getCurrentAppointmentCount() < maxAppointments;
	}

	/**
	 * Compatibility methods for Appointment code - use LocalDateTime versions
	 * These methods return LocalDateTime by combining workDate with time fields
	 */
	public LocalDateTime getStartTimeAsDateTime() {
		return getStartDateTime();
	}

	public LocalDateTime getEndTimeAsDateTime() {
		return getEndDateTime();
	}
}
