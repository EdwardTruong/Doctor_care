package com.example.doctorcare.application.service.role.query;

import org.springframework.data.domain.Pageable;

import com.example.doctorcare.application.service.role.dto.RoleDto;
import com.example.doctorcare.core.cqrs.PageQuery;

/**
 * Query để lấy danh sách roles với tìm kiếm và phân trang
 */
public record GetRolesQuery(
    String keyword,      // Từ khóa tìm kiếm theo tên role
    Boolean active,      // Lọc theo trạng thái active (null = all)
    String sortBy,       // Trường sắp xếp
    String sortDirection, // Hướng sắp xếp (asc/desc)
    Pageable pageable    // Thông tin phân trang
) implements PageQuery<RoleDto> {
}