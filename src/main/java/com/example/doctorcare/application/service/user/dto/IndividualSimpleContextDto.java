package com.example.doctorcare.application.service.user.dto;

import java.io.Serializable;


/**
 * DTO gọn nhẹ chứa thông tin định danh cơ bản của một doanh nhân.
 * Được sử dụng trong các ngữ cảnh như UserContext để tránh tải quá nhiều dữ liệu.
 */
public record IndividualSimpleContextDto(
        Long id,
        String fullName,
        String avatarUrl) implements Serializable {

}