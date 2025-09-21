package com.example.doctorcare.application.service.user.dto;

import java.io.Serializable;

import com.example.doctorcare.domain.system.user.User;


/**
 * DTO gọn nhẹ chứa thông tin định danh cơ bản của người dùng.
 * Được sử dụng trong các ngữ cảnh không yêu cầu đầy đủ chi tiết,
 * ví dụ như trong UserContext, để tối ưu hóa bộ nhớ.
 */
public record UserSimpleDto(
    Long id,
    String username,
    String fullName,
    String email,
    String avatarUrl
) implements Serializable {

    public static UserSimpleDto fromEntity(User user) {
        if (user == null) {
            return null;
        }
        // Giả định User entity có các getter tương ứng.
        // Cần kiểm tra và bổ sung nếu User entity chưa có avatarUrl.
        return new UserSimpleDto(user.getId(), user.getUsername(), user.getFullName(), user.getEmail(), user.getAvatarUrl());
    }
}