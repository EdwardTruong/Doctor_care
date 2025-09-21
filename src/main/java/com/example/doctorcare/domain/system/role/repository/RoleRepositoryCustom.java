package com.example.doctorcare.domain.system.role.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.doctorcare.domain.system.role.Role;
import com.example.doctorcare.domain.system.role.RoleType;

/**
 * Interface cho các phương thức truy vấn vai trò tùy chỉnh.
 */
public interface RoleRepositoryCustom {

    /**
     * Tìm kiếm và phân trang vai trò với các điều kiện động.
     *
     * @return Một trang các vai trò thỏa mãn điều kiện.
     */
    Page<Role> search(String keyword, RoleType roleType, Pageable pageable);
}
