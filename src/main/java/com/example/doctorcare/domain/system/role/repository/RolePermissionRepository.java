package com.example.doctorcare.domain.system.role.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.example.doctorcare.core.domain.BaseRepository;
import com.example.doctorcare.domain.system.role.model.RolePermission;


public interface RolePermissionRepository extends BaseRepository<RolePermission, Long> {

    /**
     * Finds all RolePermission entities for a given set of role IDs, eagerly fetching the associated Permission.
     * This is used to gather all authorities for a user.
     *
     * @param roleIds The set of role IDs.
     * @return A list of RolePermission entities with permissions loaded.
     */
    @Query("SELECT rp FROM RolePermission rp JOIN FETCH rp.permission p WHERE rp.role.id IN :roleIds")
    List<RolePermission> findWithPermissionsByRoleIdIn(@Param("roleIds") Set<Long> roleIds);

    /**
     * Finds all permission IDs for a given list of role IDs.
     * This is an efficient way to get all permissions for a role hierarchy in one query.
     *
     * @param roleIds The list of role IDs.
     * @return A set of permission IDs.
     */
    @Query("SELECT rp.permission.id FROM RolePermission rp WHERE rp.role.id IN :roleIds")
    Set<Long> findPermissionIdsByRoleIds(@Param("roleIds") List<Long> roleIds);

    List<RolePermission> findByRoleId(Long roleId);

    List<RolePermission> findByPermissionId(Long permissionId);
}