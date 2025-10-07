package com.example.doctorcare.domain.system.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.BatchSize;

import com.example.doctorcare.core.domain.BaseEntity;
import com.example.doctorcare.core.enums.Gender;
import com.example.doctorcare.domain.business.appointment.Appointment;
import com.example.doctorcare.domain.business.clinics_.Clinics;
import com.example.doctorcare.domain.business.doctor.Doctor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "users")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity<Long> {

	public static final int MAX_LENGTH_USERNAME = 50;
	public static final int MAX_LENGTH_EMAIL = 100;
	public static final int MAX_LENGTH_PHONE_NUMBER = 20;
	public static final int MAX_LENGTH_PASSWORD = 100;
	public static final int MAX_LENGTH_FULLNAME = 100;
	public static final int MAX_LENGTH_ADDRESS = 100;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email")
	@NotBlank(message = "Nhập địa chỉ mail")
	@Email(message = "Địa chỉ mail không đúng định dạng !", regexp = "^[\\w-\\+]+(\\.[\\w-\\+]+)*@[\\w-]+(\\.[\\w-]+)*\\.[a-zA-Z]{2,6}$")
	private String addressEmail;

	@NotNull
	@Column(name = "username", nullable = false, length = MAX_LENGTH_USERNAME)
	private String username;

	@Column(name = "encrypted_password", length = MAX_LENGTH_PASSWORD)
	private String encryptedPassword;

	@Column(name = "address")
	private String address;

	@Column(name = "fullname")
	private String fullName;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender", length = 10, columnDefinition = "VARCHAR(20)")
	private Gender gender;

	@Column(name = "phone")
	private String phone;

	@Column(name = "description")
	private String description;

	@Column(name = "avatarUrl")
	private String avatarUrl;

	@Column(name = "date_of_birth")
	@Temporal(value = TemporalType.DATE)
	private LocalDate dateOfbirth;

	@Column(name = "active", nullable = false)
	private boolean active;

    @OneToMany(mappedBy = "user")
    @BatchSize(size = 20) // Tối ưu hóa cho các trường hợp không dùng JOIN FETCH
    private Set<UserRole> userRoles;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Doctor doctor;

	@OneToMany(mappedBy = "owner")
	private Set<Clinics> client;

	@OneToMany(mappedBy = "user")
	private List<Appointment> appointment;

}
