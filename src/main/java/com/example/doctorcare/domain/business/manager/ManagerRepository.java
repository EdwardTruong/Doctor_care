package com.example.doctorcare.domain.business.manager;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.doctorcare.core.domain.BaseRepository;

@Repository
public interface ManagerRepository extends BaseRepository<Manager, Long> {
 
    boolean existsByRoleId(Long id);   

    /**
     * Tìm manager theo email
     */
    Optional<Manager> findByEmailAndDeletedFalse(String email);

    /**
     * Tìm manager theo employee code
     */
    Optional<Manager> findByEmployeeCodeAndDeletedFalse(String employeeCode);

    /**
     * Tìm tất cả manager active
     */
    List<Manager> findByStatusAndDeletedFalse(Integer status);

    /**
     * Tìm manager theo department
     */
    List<Manager> findByDepartmentAndDeletedFalse(String department);

    /**
     * Tìm manager con theo parent manager ID
     */
    List<Manager> findByParentManagerIdAndDeletedFalse(Long parentManagerId);

    /**
     * Tìm tất cả manager cấp cao (không có parent)
     */
    List<Manager> findByParentManagerIsNullAndDeletedFalse();

    /**
     * Tìm manager với phân trang và filter
     */
    @Query("SELECT m FROM Manager m WHERE " +
           "(:keyword IS NULL OR " +
           " LOWER(m.firstName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(m.lastName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(m.email) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           " LOWER(m.employeeCode) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND " +
           "(:department IS NULL OR LOWER(m.department) = LOWER(:department)) AND " +
           "(:status IS NULL OR m.status = :status) AND " +
           "m.deleted = false")
    Page<Manager> findManagersWithFilters(
        @Param("keyword") String keyword,
        @Param("department") String department,
        @Param("status") Integer status,
        Pageable pageable);

    /**
     * Tìm manager theo role name
     */
    @Query("SELECT DISTINCT m FROM Manager m " +
           "JOIN m.roles r WHERE " +
           "LOWER(r.roleName) = LOWER(:roleName) AND " +
           "m.deleted = false")
    List<Manager> findByRoleName(@Param("roleName") String roleName);

    /**
     * Đếm số doctor được quản lý bởi manager
     */
    @Query("SELECT COUNT(d) FROM Doctor d WHERE d.manager.id = :managerId")
    Long countManagedDoctorsByManagerId(@Param("managerId") Long managerId);

    /**
     * Tìm manager quản lý doctor cụ thể
     */
    @Query("SELECT m FROM Manager m " +
           "JOIN m.managedDoctors d WHERE " +
           "d.id = :doctorId AND m.deleted = false")
    Optional<Manager> findManagerByDoctorId(@Param("doctorId") Long doctorId);

    /**
     * Kiểm tra xem manager có quyền trên doctor không
     */
    @Query("SELECT CASE WHEN COUNT(d) > 0 THEN true ELSE false END " +
           "FROM Doctor d WHERE d.manager.id = :managerId AND d.id = :doctorId")
    boolean hasAuthorityOverDoctor(@Param("managerId") Long managerId, @Param("doctorId") Long doctorId);

    /**
     * Tìm manager theo multiple roles
     */
    @Query("SELECT DISTINCT m FROM Manager m " +
           "JOIN m.roles r WHERE " +
           "r.roleName IN :roleNames AND " +
           "m.deleted = false")
    List<Manager> findByRoleNames(@Param("roleNames") List<String> roleNames);

    // --- Hierarchy Queries ---

    /**
     * Tìm tất cả manager là parent (có manager con)
     */
    List<Manager> findByIsParentTrueAndDeletedFalse();

    /**
     * Tìm manager theo hierarchy level
     */
    List<Manager> findByHierarchyLevelAndDeletedFalse(Integer level);

    /**
     * Tìm tất cả manager cấp cao nhất (root managers)
     */
    @Query("SELECT m FROM Manager m WHERE " +
           "m.parentManager IS NULL AND " +
           "(m.hierarchyLevel = 0 OR m.hierarchyLevel IS NULL) AND " +
           "m.deleted = false")
    List<Manager> findRootManagers();

    /**
     * Tìm tất cả manager là leaf (không có con)
     */
    @Query("SELECT m FROM Manager m WHERE " +
           "m.isParent = false AND " +
           "SIZE(m.childManagers) = 0 AND " +
           "m.deleted = false")
    List<Manager> findLeafManagers();

    /**
     * Tìm manager theo khoảng hierarchy level
     */
    @Query("SELECT m FROM Manager m WHERE " +
           "m.hierarchyLevel >= :minLevel AND " +
           "m.hierarchyLevel <= :maxLevel AND " +
           "m.deleted = false " +
           "ORDER BY m.hierarchyLevel ASC")
    List<Manager> findByHierarchyLevelRange(@Param("minLevel") Integer minLevel, @Param("maxLevel") Integer maxLevel);

    /**
     * Đếm số manager con trực tiếp
     */
    @Query("SELECT COUNT(m) FROM Manager m WHERE " +
           "m.parentManager.id = :parentId AND " +
           "m.deleted = false")
    Long countDirectChildren(@Param("parentId") Long parentId);

    /**
     * Tìm tất cả manager trong cùng một department và hierarchy level
     */
    @Query("SELECT m FROM Manager m WHERE " +
           "LOWER(m.department) = LOWER(:department) AND " +
           "m.hierarchyLevel = :level AND " +
           "m.deleted = false")
    List<Manager> findByDepartmentAndHierarchyLevel(@Param("department") String department, @Param("level") Integer level);

    /**
     * Tìm manager có nhiều manager con nhất
     */
    @Query("SELECT m FROM Manager m WHERE " +
           "m.isParent = true AND " +
           "m.deleted = false " +
           "ORDER BY SIZE(m.childManagers) DESC")
    List<Manager> findManagersOrderByChildrenCount();

    /**
     * Kiểm tra manager có phải là ancestor của manager khác không
     */
    @Query(value = "WITH RECURSIVE manager_path AS ( " +
           "  SELECT id, parent_manager_id, 0 as depth " +
           "  FROM managers WHERE id = :descendantId AND deleted = false " +
           "  UNION ALL " +
           "  SELECT m.id, m.parent_manager_id, mp.depth + 1 " +
           "  FROM managers m " +
           "  INNER JOIN manager_path mp ON m.id = mp.parent_manager_id " +
           "  WHERE m.deleted = false " +
           ") " +
           "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END " +
           "FROM manager_path WHERE id = :ancestorId",
           nativeQuery = true)
    boolean isAncestorOf(@Param("ancestorId") Long ancestorId, @Param("descendantId") Long descendantId);

    /**
     * Kiểm tra email tồn tại (trừ manager cụ thể)
     */
    boolean existsByEmailAndIdNot(String email, Long id);

    /**
     * Kiểm tra employee code tồn tại (trừ manager cụ thể) 
     */
    boolean existsByEmployeeCodeAndIdNot(String employeeCode, Long id);

    /**
     * Tìm manager theo id và active
     */
    Optional<Manager> findByIdAndIsActiveTrue(Long id);

    /**
     * Tìm hierarchy của manager từ root id cụ thể
     */
    @Query(value = "WITH RECURSIVE manager_hierarchy AS ( " +
           "  SELECT * FROM managers WHERE id = :rootId AND is_active = true " +
           "  UNION ALL " +
           "  SELECT m.* FROM managers m " +
           "  INNER JOIN manager_hierarchy mh ON m.parent_manager_id = mh.id " +
           "  WHERE m.is_active = true " +
           ") " +
           "SELECT * FROM manager_hierarchy ORDER BY level",
           nativeQuery = true)
    List<Manager> findManagersHierarchy(@Param("rootId") Long rootId);

    /**
     * Tìm tất cả manager active
     */
    @Query("SELECT m FROM Manager m WHERE m.isActive = true ORDER BY m.level, m.fullName")
    List<Manager> findAllActiveManagers();

}
