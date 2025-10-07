package com.example.doctorcare.domain.business.manager;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.doctorcare.core.domain.BaseEntity;
import com.example.doctorcare.domain.system.role.model.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * Entity quản lý quan hệ many-to-many giữa Manager và Role
 * với thêm thông tin về thời gian gán role và người gán
 */
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
@Table(name = "manager_roles")
@Getter
@Setter
@Builder
public class ManagerRole extends BaseEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "manager_id", nullable = false)
    Manager manager;

    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    Role role;

    @Column(name = "assigned_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime assignedAt;

    @Column(name = "assigned_by", length = 100)
    String assignedBy; // Email hoặc ID của người gán role

    @Column(name = "expires_at")
    @Temporal(value = TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime expiresAt; // Thời gian hết hạn role (nếu có)

    @Column(name = "is_active")
    Boolean isActive; // Role có active không

    @Column(name = "notes", length = 500)
    String notes; // Ghi chú về việc gán role

    /**
     * Kiểm tra xem role assignment có còn valid không
     */
    public boolean isValid() {
        if (isActive == null || !isActive) {
            return false;
        }
        
        if (expiresAt != null) {
            return LocalDateTime.now().isBefore(expiresAt);
        }
        
        return true;
    }

    /**
     * Activate role assignment
     */
    public void activate(String assignedBy) {
        this.isActive = true;
        this.assignedAt = LocalDateTime.now();
        this.assignedBy = assignedBy;
    }

    /**
     * Deactivate role assignment
     */
    public void deactivate() {
        this.isActive = false;
    }

    /**
     * Set expiry date cho role
     */
    public void setExpiry(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }

    @Override
    public String toString() {
        return "ManagerRole{" +
                "id=" + id +
                ", managerId=" + (manager != null ? manager.getId() : null) +
                ", roleId=" + (role != null ? role.getId() : null) +
                ", assignedAt=" + assignedAt +
                ", assignedBy='" + assignedBy + '\'' +
                ", expiresAt=" + expiresAt +
                ", isActive=" + isActive +
                '}';
    }
}