package com.example.doctorcare.application.service.manager.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.doctorcare.application.service.role.dto.RoleDto;
import com.example.doctorcare.domain.business.manager.Manager;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ManagerDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String fullName;
    private String email;
    private Integer phoneNumber;
    private String address;
    private String employeeCode;
    private String department;
    private String position;
    private LocalDateTime hireDate;
    private Integer status;
    private String statusText;
    private Double salary;
    private String notes;

    // Hierarchy fields
    private Boolean isParent;
    private Integer hierarchyLevel;
    private Long parentManagerId;
    private String parentManagerName;
    
    // Counts
    private Integer childrenCount;
    private Integer managedDoctorsCount;
    private Integer totalDescendantsCount;

    // Collections (optional, load on demand)
    private List<ManagerDto> children;
    private Set<RoleDto> roles;
    private List<String> roleNames;

    // Utility fields
    private Boolean isRoot;
    private Boolean isLeaf;
    private Boolean isActive;

    /**
     * Convert từ Manager entity sang ManagerDto (basic info)
     */
    public static ManagerDto fromEntity(Manager manager) {
        if (manager == null) return null;

        return ManagerDto.builder()
                .id(manager.getId())
                .firstName(manager.getFirstName())
                .lastName(manager.getLastName())
                .fullName(manager.getFullName())
                .email(manager.getEmail())
                .phoneNumber(manager.getPhoneNumber())
                .address(manager.getAddress())
                .employeeCode(manager.getEmployeeCode())
                .department(manager.getDepartment())
                .position(manager.getPosition())
                .hireDate(manager.getHireDate())
                .status(manager.getStatus())
                .statusText(getStatusText(manager.getStatus()))
                .salary(manager.getSalary())
                .notes(manager.getNotes())
                
                // Hierarchy info
                .isParent(manager.getChildManagers() != null && !manager.getChildManagers().isEmpty())
                .hierarchyLevel(manager.getHierarchyLevel())
                .parentManagerId(manager.getParentManager() != null ? manager.getParentManager().getId() : null)
                .parentManagerName(manager.getParentManager() != null ? manager.getParentManager().getFullName() : null)
                
                // Counts
                .childrenCount(manager.getChildManagers() != null ? manager.getChildManagers().size() : 0)
                .managedDoctorsCount(manager.getManagedDoctors() != null ? manager.getManagedDoctors().size() : 0)
                .totalDescendantsCount(calculateTotalDescendants(manager))
                
                // Utility flags
                .isRoot(manager.getParentManager() == null)
                .isLeaf(manager.getChildManagers() == null || manager.getChildManagers().isEmpty())
                .isActive(manager.isActive())
                
                // Role names only (not full RoleDto to avoid circular reference)
                .roleNames(manager.getRoles() != null ? 
                    manager.getRoles().stream()
                        .map(role -> role.getRoleName())
                        .collect(Collectors.toList()) : null)
                .build();
    }

    /**
     * Convert với đầy đủ thông tin children và roles
     */
    public static ManagerDto fromEntityWithDetails(Manager manager) {
        ManagerDto dto = fromEntity(manager);
        if (manager == null) return dto;

        // Add children (recursive có thể gây performance issue, nên cẩn thận)
        if (manager.getChildManagers() != null && !manager.getChildManagers().isEmpty()) {
            dto.setChildren(
                manager.getChildManagers().stream()
                    .map(ManagerDto::fromEntity) // Không recursive để tránh vòng lặp vô hạn
                    .collect(Collectors.toList())
            );
        }

        // Add full role info
        if (manager.getRoles() != null && !manager.getRoles().isEmpty()) {
            dto.setRoles(
                manager.getRoles().stream()
                    .map(RoleDto::fromEntity)
                    .collect(Collectors.toSet())
            );
        }

        return dto;
    }

    /**
     * Convert danh sách entities sang DTOs
     */
    public static List<ManagerDto> fromEntities(List<Manager> managers) {
        if (managers == null) return null;
        return managers.stream()
                .map(ManagerDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Get status text từ status number
     */
    private static String getStatusText(Integer status) {
        if (status == null) return "Unknown";
        return switch (status) {
            case 0 -> "Inactive";
            case 1 -> "Active";
            case 2 -> "Suspended";
            default -> "Unknown";
        };
    }

    // Helper methods for display
    public String getHierarchyDisplayText() {
        String levelText = hierarchyLevel != null ? "Level " + hierarchyLevel : "N/A";
        String parentText = isParent != null && isParent ? " (Parent)" : "";
        return levelText + parentText;
    }

    public String getManagerSummary() {
        return String.format("%s (%s) - %s - %s", 
            fullName, employeeCode, position, department);
    }

    /**
     * Calculate total descendants count recursively
     */
    private static Integer calculateTotalDescendants(Manager manager) {
        if (manager.getChildManagers() == null || manager.getChildManagers().isEmpty()) {
            return 0;
        }
        
        int total = manager.getChildManagers().size();
        for (Manager child : manager.getChildManagers()) {
            total += calculateTotalDescendants(child);
        }
        return total;
    }
}
