package com.example.doctorcare.domain.system.role.model;

import java.util.HashSet;
import java.util.Set;

import com.example.doctorcare.core.domain.BaseEntity;
import com.example.doctorcare.domain.business.manager.Manager;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "role") // Đổi tên bảng thành "role" cho nhất quán với các entity khác
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = { "id" }, callSuper = true)
@Builder
@Data
public class Role extends BaseEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    @Column(name = "description")
    private String description;

    /**
     * Khóa hệ thống, bất biến, dùng để định danh các vai trò có logic đặc biệt trong code.
     * Ví dụ: "SUPER_ADMIN", "ENTERPRISE_ADMIN".
     * Trường này là null đối với các vai trò do người dùng tự tạo.
     */
    @Column(name = "system_key", unique = true)
    private String systemKey;

    /**
     * Phân loại vai trò để xác định ngữ cảnh áp dụng (Doanh nghiệp, Tổ chức, hoặc Toàn cục).
     * Null nếu là vai trò toàn cục (ví dụ: SUPER_ADMIN).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role_type", length = 20, columnDefinition = "VARCHAR(20)")
    private RoleType roleType;
    /**
     * Vai trò cha. Một vai trò có thể kế thừa quyền từ vai trò cha của nó.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_role_id", nullable = true)
    private Role parentRole;

    /**
     * Danh sách các vai trò con.
     * <p>
     * - {@code mappedBy = "parentRole"}: Chỉ định rằng mối quan hệ này được quản lý bởi trường 'parentRole' ở phía vai trò con (Role).
     * Điều này giúp JPA biết cách liên kết và tránh tạo bảng trung gian không cần thiết.
     * <p>
     * - {@code cascade = CascadeType.ALL}: Bất kỳ hành động nào (lưu, cập nhật, xóa) trên vai trò cha sẽ được tự động áp dụng cho các vai trò con trong tập hợp này.
     * <p>
     * - {@code orphanRemoval = true}: Nếu một vai trò con bị gỡ bỏ khỏi tập hợp {@code children} này (ví dụ: {@code parent.getChildren().remove(child)}),
     * nó sẽ tự động bị xóa khỏi cơ sở dữ liệu.
     */
    @OneToMany(mappedBy = "parentRole", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude // Ngăn Lombok tạo vòng lặp vô hạn trong toString()
    @JsonIgnore // Bỏ qua trường này khi serialize/deserialize để tránh LazyInitializationException khi đọc từ cache
    private Set<Role> children = new HashSet<>();

    /**
     * Danh sách các manager có vai trò này.
     * Quan hệ many-to-many với Manager.
     */
    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private Set<Manager> managers = new HashSet<>();
    
    /**
     * Relationship with RolePermission for permission assignments.
     * This allows fetching all permissions assigned to this role.
     */
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @JsonIgnore
    private Set<RolePermission> rolePermissions = new HashSet<>();

    public Role(String roleName, String description) {
        if (roleName == null || roleName.isEmpty()) {
            throw new IllegalArgumentException("Role name cannot be null or empty");
        }
        this.roleName = roleName;
        this.description = description;
    }

    // --- Các phương thức tiện ích để quản lý mối quan hệ ---

    /**
     * Thêm một vai trò con vào vai trò hiện tại.
     * Phương thức này đảm bảo tính nhất quán của mối quan hệ hai chiều.
     *
     * @param child Vai trò con cần thêm.
     */
    public void addChild(Role child) {
        children.add(child);
        child.setParentRole(this);
    }

    /**
     * Xóa một vai trò con khỏi vai trò hiện tại.
     *
     * @param child Vai trò con cần xóa.
     */
    public void removeChild(Role child) {
        children.remove(child);
        child.setParentRole(null);
    }

    /**
     * Thêm manager vào vai trò này.
     * Phương thức này đảm bảo tính nhất quán của mối quan hệ hai chiều.
     *
     * @param manager Manager cần thêm.
     */
    public void addManager(Manager manager) {
        if (manager != null) {
            managers.add(manager);
            manager.getRoles().add(this);
        }
    }

    /**
     * Xóa manager khỏi vai trò này.
     *
     * @param manager Manager cần xóa.
     */
    public void removeManager(Manager manager) {
        if (manager != null) {
            managers.remove(manager);
            manager.getRoles().remove(this);
        }
    }
}
