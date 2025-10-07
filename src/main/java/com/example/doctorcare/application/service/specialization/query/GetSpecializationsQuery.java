package com.example.doctorcare.application.service.specialization.query;

import org.springframework.data.domain.Pageable;

import com.example.doctorcare.application.service.specialization.dto.SpecializationsDto;
import com.example.doctorcare.core.cqrs.PageQuery;

/**
 * Query để lấy danh sách chuyên khoa với tìm kiếm và phân trang
 */
public record GetSpecializationsQuery(
    String keyword,      // Từ khóa tìm kiếm theo tên chuyên khoa
    String sortBy,       // Trường sắp xếp
    String sortDirection, // Hướng sắp xếp (asc/desc)
    Pageable pageable    // Thông tin phân trang
) implements PageQuery<SpecializationsDto> {
}