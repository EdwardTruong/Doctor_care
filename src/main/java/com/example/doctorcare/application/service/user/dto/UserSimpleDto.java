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
    String addressEmail,
    String avatarUrl
) implements Serializable {

    public static UserSimpleDto fromEntity(User user) {
        if (user == null) {
            return null;
        }
        return new UserSimpleDto(user.getId(), user.getUsername(), user.getFullName(), user.getAddressEmail(), user.getAvatarUrl());
    }
}