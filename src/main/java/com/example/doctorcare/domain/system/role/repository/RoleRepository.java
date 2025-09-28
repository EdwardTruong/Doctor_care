package com.example.doctorcare.domain.system.role.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.doctorcare.core.domain.BaseRepository;
import com.example.doctorcare.domain.system.role.model.Role;


public interface RoleRepository extends BaseRepository<Role, Long>, RoleRepositoryCustom{


    Optional<Role> findByIdAndDeletedFalse(Long id);

    Optional<Role> findByNameAndDeletedFalse(String name);

    /**
     * Tìm tất cả các vai trò con trực tiếp của một vai trò cha.
     *
     * @param parentRoleId ID của vai trò cha.
     * @return Danh sách các vai trò con.
     */
    List<Role> findByParentRoleId(Long parentRoleId);

        /**
     * Kiểm tra xem một vai trò có bất kỳ vai trò con nào không.
     *
     * @param parentRoleId ID của vai trò cha.
     * @return true nếu có ít nhất một vai trò con, ngược lại false.
     */
    boolean existsByParentRoleId(Long parentRoleId);

     /**
     * Sử dụng một truy vấn đệ quy (Recursive CTE) để lấy tất cả các vai trò
     * trong một hệ thống phân cấp, bắt đầu từ một roleId cụ thể.
     * Đây là cách hiệu quả nhất để lấy toàn bộ cây vai trò trong một lần gọi DB.
     * LƯU Ý: Cú pháp có thể thay đổi tùy thuộc vào CSDL (PostgreSQL, MySQL 8+, SQL Server).
     * Ví dụ dưới đây dành cho PostgreSQL.
     */
    @Query(value = """
        WITH RECURSIVE role_hierarchy AS (
            SELECT id, parent_role_id FROM role WHERE id = :roleId
            UNION ALL
            SELECT r.id, r.parent_role_id FROM role r JOIN role_hierarchy rh ON r.id = rh.parent_role_id
        )
        SELECT id FROM role_hierarchy
    """, nativeQuery = true)
    List<Long> findAllRoleIdsInHierarchy(@Param("roleId") Long roleId);
    
    /**
     * Sử dụng một truy vấn đệ quy (Recursive CTE) để lấy tất cả các vai trò con
     * trong một hệ thống phân cấp, bao gồm cả chính nó.
     * Đây là cách hiệu quả nhất để lấy toàn bộ cây vai trò con trong một lần gọi DB.
     * LƯU Ý: Cú pháp có thể thay đổi tùy thuộc vào CSDL (PostgreSQL, MySQL 8+, SQL Server).
     */
    @Query(value = """
        WITH RECURSIVE role_descendants AS (
            SELECT id, parent_role_id FROM role WHERE id = :roleId
            UNION ALL
            SELECT r.id, r.parent_role_id FROM role r JOIN role_descendants rd ON r.parent_role_id = rd.id
        )
        SELECT id FROM role_descendants
    """, nativeQuery = true)
    List<Long> findAllDescendantRoleIdsIncludingSelf(@Param("roleId") Long roleId);

}
