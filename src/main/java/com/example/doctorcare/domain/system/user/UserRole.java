package com.example.doctorcare.domain.system.user;


import com.example.doctorcare.core.domain.BaseEntity;
import com.example.doctorcare.domain.system.role.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Entity này đại diện cho mối quan hệ trực tiếp giữa một Người dùng (User) và một Vai trò (Role).
 * Nó được sử dụng để gán các vai trò toàn cục (global roles) cho người dùng,
 * không phụ thuộc vào bất kỳ ngữ cảnh cụ thể nào như Doanh nghiệp hay Tổ chức.
 * <p>
 * Ví dụ: Gán vai trò SUPER_ADMIN, ADMIN cho người dùng quản trị hệ thống.
 */
@Entity
@Table(name = "user_roles")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserRole extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Người dùng được gán vai trò.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * Vai trò được gán cho người dùng.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

}
