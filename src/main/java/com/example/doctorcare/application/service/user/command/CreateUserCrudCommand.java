package com.example.doctorcare.application.service.user.command;

import java.time.LocalDate;

import com.example.doctorcare.application.service.user.dto.UserDetailDto;
import com.example.doctorcare.core.cqrs.CommandWithResult;
import com.example.doctorcare.core.enums.Gender;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Command để tạo user mới cho CRUD operations
 */
public record CreateUserCrudCommand(
    @NotBlank(message = "Tên đăng nhập không được để trống")
    @Size(max = 50, message = "Tên đăng nhập không được vượt quá 50 ký tự")
    String username,
    
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(max = 100, message = "Email không được vượt quá 100 ký tự")
    String addressEmail,
    
    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 100, message = "Mật khẩu phải có từ 6-100 ký tự")
    String password,
    
    @Size(max = 100, message = "Họ tên không được vượt quá 100 ký tự")
    String fullName,
    
    @Size(max = 100, message = "Địa chỉ không được vượt quá 100 ký tự")
    String address,
    
    @Size(max = 20, message = "Số điện thoại không được vượt quá 20 ký tự")
    String phone,
    
    Gender gender,
    LocalDate dateOfbirth,
    
    @Size(max = 500, message = "Mô tả không được vượt quá 500 ký tự")
    String description,
    
    @Size(max = 255, message = "URL avatar không được vượt quá 255 ký tự")
    String avatarUrl,
    
    Boolean active // Default will be true if null
) implements CommandWithResult<UserDetailDto> {
}