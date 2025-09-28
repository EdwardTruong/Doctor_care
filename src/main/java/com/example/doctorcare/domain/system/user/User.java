package com.example.doctorcare.domain.system.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.BatchSize;

import com.example.doctorcare.core.domain.BaseEntity;
import com.example.doctorcare.core.enums.Gender;
import com.example.doctorcare.domain.business.clinics.Clinics;
import com.example.doctorcare.domain.business.doctor.model.Doctor;
import com.example.doctorcare.domain.business.status_schedule.Statuses;

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
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity<Long> {

	public static final int MAX_LENGTH_USERNAME = 50;
	public static final int MAX_LENGTH_EMAIL = 100;
	public static final int MAX_LENGTH_PHONE_NUMBER = 20;
	public static final int MAX_LENGTH_PASSWORD = 100;
	public static final int MAX_LENGTH_FULLNAME = 100;
	public static final int MAX_LENGTH_ADDRESS = 100;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(name = "email")
	@NotBlank(message = "Nhập địa chỉ mail")
	@Email(message = "Địa chỉ mail không đúng định dạng !", regexp = "^[\\w-\\+]+(\\.[\\w-\\+]+)*@[\\w-]+(\\.[\\w-]+)*\\.[a-zA-Z]{2,6}$")
	String email;

	@NotNull
	@Column(name = "username", nullable = false, length = MAX_LENGTH_USERNAME)
	String username;

	@Column(name = "encrypted_password", length = MAX_LENGTH_PASSWORD)
	String encryptedPassword;

	@Column(name = "address")
	String address;

	@Column(name = "fullname")
	String fullName;

	@Enumerated(EnumType.STRING)
	@Column(name = "gender", length = 10, columnDefinition = "VARCHAR(20)")
	Gender gender;

	@Column(name = "phone")
	String phone;

	@Column(name = "description")
	String description;

	@Column(name = "avatarUrl")
	String avatarUrl;

	@Column(name = "date_of_birth")
	@Temporal(value = TemporalType.DATE)
	LocalDate dateOfbirth;

	@Column(name = "active", nullable = false)
	boolean active;

    @OneToMany(mappedBy = "user")
    @BatchSize(size = 20) // Tối ưu hóa cho các trường hợp không dùng JOIN FETCH
    private Set<UserRole> userRoles;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	Doctor doctor;

	@OneToMany(mappedBy = "owner")
	Set<Clinics> client;

	@OneToMany(mappedBy = "user")
	List<Statuses> statuses;

	public void addStatus(Statuses status) {
		if (statuses == null) {
			statuses = new ArrayList<>();
		}
		statuses.add(status);
		status.setUser(this);
	}
}
