package com.example.doctorcare.application.service.manager.command;

import java.time.LocalDateTime;
import java.util.Set;

import com.example.doctorcare.application.service.manager.dto.ManagerDto;
import com.example.doctorcare.core.cqrs.CommandWithResult;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Command để tạo manager mới
 */
public record CreateManagerCommand(
    
    @NotBlank(message = "Tên không được để trống")
    @Size(max = 50, message = "Tên không được vượt quá 50 ký tự")
    String firstName,
    
    @NotBlank(message = "Họ không được để trống")
    @Size(max = 50, message = "Họ không được vượt quá 50 ký tự")
    String lastName,
    
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không hợp lệ")
    @Size(max = 100, message = "Email không được vượt quá 100 ký tự")
    String email,
    
    @Size(max = 20, message = "Số điện thoại không được vượt quá 20 ký tự")
    String phone,
    
    @Size(max = 255, message = "Địa chỉ không được vượt quá 255 ký tự")
    String address,
    
    @Size(max = 20, message = "Mã nhân viên không được vượt quá 20 ký tự")
    String employeeCode,
    
    @NotBlank(message = "Phòng ban không được để trống")
    @Size(max = 100, message = "Phòng ban không được vượt quá 100 ký tự")
    String department,
    
    @NotBlank(message = "Chức vụ không được để trống")
    @Size(max = 100, message = "Chức vụ không được vượt quá 100 ký tự")
    String position,
    
    LocalDateTime hireDate,
    
    @NotNull(message = "Trạng thái không được để trống")
    Integer status, // 0: Inactive, 1: Active, 2: Suspended
    
    Double salary,
    
    @Size(max = 500, message = "Ghi chú không được vượt quá 500 ký tự")
    String notes,
    
    // Hierarchy fields
    Long parentManagerId,
    Integer hierarchyLevel,
    
    // Role assignments
    Set<Long> roleIds
    
) implements CommandWithResult<ManagerDto> {
    
    /**
     * Validate business rules
     */
    public void validate() {
        if (status != null && (status < 0 || status > 2)) {
            throw new IllegalArgumentException("Trạng thái phải là 0 (Inactive), 1 (Active), hoặc 2 (Suspended)");
        }
        
        if (salary != null && salary < 0) {
            throw new IllegalArgumentException("Lương không thể âm");
        }
        
        if (hierarchyLevel != null && hierarchyLevel < 0) {
            throw new IllegalArgumentException("Cấp độ phân cấp không thể âm");
        }
        
        if (hireDate != null && hireDate.isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Ngày tuyển dụng không thể ở tương lai");
        }
    }
}