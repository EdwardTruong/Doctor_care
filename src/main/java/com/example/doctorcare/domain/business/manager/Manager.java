package com.example.doctorcare.domain.business.manager;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.doctorcare.core.domain.BaseEntity;
import com.example.doctorcare.domain.business.doctor.Doctor;
import com.example.doctorcare.domain.system.role.model.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

@Table(name = "managers")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Manager extends BaseEntity<Long> {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    String lastName;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    String email;

    @Column(name = "phone", length = 20)
    String phone;

    @Column(name = "address", length = 255)
    String address;

    @Column(name = "employee_code", unique = true, length = 20)
    String employeeCode;

	@Column(name = "phoneNumber")
	Integer phoneNumber;

    @Column(name = "department", length = 100)
    String department;

    @Column(name = "position", length = 100)
    String position;

    @Column(name = "hire_date")
    @Temporal(value = TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime hireDate;

    @Column(name = "status")
    Integer status; // 0: Inactive, 1: Active, 2: Suspended

    @Column(name = "salary")
    Double salary;

    @Column(name = "notes", length = 500)
    String notes;

    // Field đánh dấu manager này là cha của các cấp con
    @Column(name = "is_parent", nullable = false)
    @Builder.Default
    Boolean isParent = false;

    // Cấp độ trong hierarchy (0 = cấp cao nhất, 1 = cấp 2, ...)
    @Column(name = "hierarchy_level")
    @Builder.Default
    Integer hierarchyLevel = 0;

    // Quan hệ hierarchical - Manager có thể có manager cha
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_manager_id")
    @JsonBackReference("manager-parent")
    Manager parentManager;

    // Danh sách manager con
    @OneToMany(mappedBy = "parentManager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("manager-parent")
    @Builder.Default
    @ToString.Exclude
    List<Manager> childManagers = new java.util.ArrayList<>();

    // Quan hệ many-to-many với Role
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "manager_roles",
        joinColumns = @JoinColumn(name = "manager_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    @ToString.Exclude
    Set<Role> roles = new HashSet<>();

    // Danh sách doctor được quản lý
    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("manager-doctors")
    @Builder.Default
    @ToString.Exclude
    List<Doctor> managedDoctors = new java.util.ArrayList<>();

    // Helper methods for managing relationships

    /**
     * Thêm một manager con
     */
    public void addChildManager(Manager childManager) {
        if (childManager != null) {
            this.childManagers.add(childManager);
            childManager.setParentManager(this);
            childManager.setHierarchyLevel(this.hierarchyLevel + 1);
            
            // Đánh dấu manager này là parent
            this.setIsParent(true);
        }
    }

    /**
     * Xóa một manager con
     */
    public void removeChildManager(Manager childManager) {
        if (childManager != null) {
            this.childManagers.remove(childManager);
            childManager.setParentManager(null);
            childManager.setHierarchyLevel(0); // Reset level
            
            // Nếu không còn manager con nào thì bỏ đánh dấu parent
            if (this.childManagers.isEmpty()) {
                this.setIsParent(false);
            }
        }
    }

    /**
     * Thêm role cho manager
     */
    public void addRole(Role role) {
        if (role != null) {
            this.roles.add(role);
            role.getManagers().add(this);
        }
    }

    /**
     * Xóa role khỏi manager
     */
    public void removeRole(Role role) {
        if (role != null) {
            this.roles.remove(role);
            role.getManagers().remove(this);
        }
    }

    /**
     * Thêm doctor vào danh sách quản lý
     */
    public void addManagedDoctor(Doctor doctor) {
        if (doctor != null) {
            this.managedDoctors.add(doctor);
            doctor.setManager(this);
        }
    }

    /**
     * Xóa doctor khỏi danh sách quản lý
     */
    public void removeManagedDoctor(Doctor doctor) {
        if (doctor != null) {
            this.managedDoctors.remove(doctor);
            doctor.setManager(null);
        }
    }

    /**
     * Kiểm tra xem manager có role cụ thể không
     */
    public boolean hasRole(String roleName) {
        return roles.stream()
                .anyMatch(role -> role.getRoleName().equalsIgnoreCase(roleName));
    }

    /**
     * Lấy tên đầy đủ của manager
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    /**
     * Kiểm tra xem manager có active không
     */
    public boolean isActive() {
        return status != null && status == 1;
    }

    // --- Hierarchy Management Methods ---

    /**
     * Kiểm tra xem manager này có phải là cha/parent không
     */
    public boolean isParentManager() {
        return isParent != null && isParent && !childManagers.isEmpty();
    }

    /**
     * Kiểm tra xem manager này có phải là cấp cao nhất (root) không
     */
    public boolean isRootManager() {
        return parentManager == null && (hierarchyLevel == null || hierarchyLevel == 0);
    }

    /**
     * Kiểm tra xem manager này có phải là nhân viên cơ sở (leaf node) không
     */
    public boolean isLeafManager() {
        return childManagers.isEmpty() && (isParent == null || !isParent);
    }

    /**
     * Lấy tất cả manager con ở tất cả các cấp (recursive)
     */
    public List<Manager> getAllDescendants() {
        List<Manager> descendants = new java.util.ArrayList<>();
        for (Manager child : childManagers) {
            descendants.add(child);
            descendants.addAll(child.getAllDescendants());
        }
        return descendants;
    }

    /**
     * Lấy tất cả manager cha lên đến root (path to root)
     */
    public List<Manager> getPathToRoot() {
        List<Manager> path = new java.util.ArrayList<>();
        Manager current = this.parentManager;
        while (current != null) {
            path.add(current);
            current = current.getParentManager();
        }
        return path;
    }

    /**
     * Đếm tổng số manager con ở tất cả các cấp
     */
    public int getTotalDescendantsCount() {
        return getAllDescendants().size();
    }

    /**
     * Cập nhật lại hierarchy level cho tất cả các manager con
     */
    public void updateChildrenHierarchyLevels() {
        for (Manager child : childManagers) {
            child.setHierarchyLevel(this.hierarchyLevel + 1);
            child.updateChildrenHierarchyLevels(); // Recursive update
        }
    }

    /**
     * Kiểm tra xem manager khác có phải là con của manager này không (direct hoặc indirect)
     */
    public boolean isAncestorOf(Manager otherManager) {
        if (otherManager == null) return false;
        
        // Kiểm tra direct children
        if (childManagers.contains(otherManager)) {
            return true;
        }
        
        // Kiểm tra indirect descendants
        return getAllDescendants().contains(otherManager);
    }

    @Override
    public String toString() {
        return "Manager{" +
                "id=" + id +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", employeeCode='" + employeeCode + '\'' +
                ", department='" + department + '\'' +
                ", position='" + position + '\'' +
                ", status=" + status +
                '}';
    }

}
