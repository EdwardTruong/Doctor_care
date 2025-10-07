package com.example.doctorcare.application.service.manager.query;

import org.springframework.data.domain.Pageable;

import com.example.doctorcare.application.service.manager.dto.ManagerDto;
import com.example.doctorcare.core.cqrs.PageQuery;

/**
 * Query để lấy danh sách managers với phân trang và filter
 */
public record GetManagersQuery(
    
    String keyword,           // Tìm kiếm theo tên, email, mã nhân viên
    String department,        // Lọc theo phòng ban  
    Integer status,           // Lọc theo trạng thái
    Integer hierarchyLevel,   // Lọc theo cấp độ
    Boolean isParent,         // Lọc manager có con hay không
    Long parentManagerId,     // Lọc theo parent manager
    String roleName,          // Lọc theo role
    Pageable pageable
    
) implements PageQuery<ManagerDto> {}