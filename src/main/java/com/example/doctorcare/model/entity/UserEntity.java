package com.example.doctorcare.model.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "user")
@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserEntity extends BaseEntity {

	@Column(name = "email")
	@NotBlank(message = "Nhập địa chỉ mail")
	@Email(message = "Địa chỉ mail không đúng định dạng !", regexp = "^[\\w-\\+]+(\\.[\\w-\\+]+)*@[\\w-]+(\\.[\\w-]+)*\\.[a-zA-Z]{2,6}$")
	String email;

	@JsonIgnore
	@Column(name = "password")
	String password;

	@Column(name = "address")
	String address;

	@Column(name = "name")
	String name;

	@Column(name = "gender")
	String genderl;

	@Column(name = "phone")
	String phone;

	@Column(name = "description")
	String description;

	@Column(name = "avatar")
	String avatar;

	@Column(name = "active")
	int active;

	@Column(name = "delete_at")
	@Temporal(value = TemporalType.TIMESTAMP)
	LocalDateTime deleteAt;

	@Column(name = "date_of_birth")
	@Temporal(value = TemporalType.DATE)
	LocalDate dateOfbirth;

	@ManyToMany(fetch = FetchType.EAGER, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST,
			CascadeType.REFRESH })
	@JsonBackReference
	@Builder.Default
	@JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	Set<RoleEntity> roles = new HashSet<>();

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	DoctorEntity doctorEntity;

	@OneToMany(mappedBy = "user")
	List<Statuses> statuses;

	public void addStatus(Statuses status) {
		if (statuses == null) {
			statuses = new ArrayList<>();
		}
		statuses.add(status);
		status.setUser(this);
	}

	@Override
	public String toString() {
		return "UserEntity [email=" + email + ", password=" + password + ", address=" + address + ", name=" + name
				+ ", genderl=" + genderl + ", phone=" + phone + ", description=" + description + ", avatar="
				+ avatar + ", active=" + active + ", createdAt=" + createdAt + ", updateAt=" + updatedAt
				+ ", doctorEntity=" + doctorEntity + ", statuses=" + statuses + "]";
	}

}
