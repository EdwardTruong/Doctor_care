package com.example.doctorcare.application.service.user.query;

import org.springframework.data.domain.Pageable;

import com.example.doctorcare.application.service.user.dto.UserDetailDto;
import com.example.doctorcare.core.cqrs.PageQuery;
import com.example.doctorcare.core.enums.Gender;

/**
 * Query để lấy danh sách users với tìm kiếm và phân trang
 */
public record GetUsersQuery(
    String keyword,      // Từ khóa tìm kiếm theo tên, email, username
    Boolean active,      // Lọc theo trạng thái active
    Gender gender,       // Lọc theo giới tính
    String sortBy,       // Trường sắp xếp
    String sortDirection, // Hướng sắp xếp (asc/desc)
    Pageable pageable    // Thông tin phân trang
) implements PageQuery<UserDetailDto> {
}