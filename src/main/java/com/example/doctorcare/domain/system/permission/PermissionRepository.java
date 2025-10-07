package com.example.doctorcare.domain.system.permission;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.doctorcare.core.domain.BaseRepository;
import com.example.doctorcare.core.enums.Action;
import com.example.doctorcare.core.enums.Resource;
import com.example.doctorcare.core.enums.Scope;

/**
 * Repository interface for managing Permission entities.
 * Provides CRUD operations and custom queries for permission management.
 */
public interface PermissionRepository extends BaseRepository<Permission, Long> {

    /**
     * Find permission by name (ignoring soft deleted)
     */
    Optional<Permission> findByNameAndDeletedFalse(String name);

    /**
     * Find all permissions by resource type
     */
    List<Permission> findByResourceAndDeletedFalse(Resource resource);

    /**
     * Find all permissions by action type
     */
    List<Permission> findByActionAndDeletedFalse(Action action);

    /**
     * Find all permissions by scope type
     */
    List<Permission> findByScopeAndDeletedFalse(Scope scope);

    /**
     * Find permission by action, resource and scope combination
     */
    Optional<Permission> findByActionAndResourceAndScopeAndDeletedFalse(
            Action action, Resource resource, Scope scope);

    /**
     * Find all permissions for a specific role
     */
    @Query("SELECT p FROM Permission p " +
           "JOIN p.rolePermissions rp " +
           "JOIN rp.role r " +
           "WHERE r.id = :roleId AND p.deleted = false")
    List<Permission> findByRoleId(@Param("roleId") Long roleId);

    /**
     * Find all permissions for multiple roles
     */
    @Query("SELECT DISTINCT p FROM Permission p " +
           "JOIN p.rolePermissions rp " +
           "JOIN rp.role r " +
           "WHERE r.id IN :roleIds AND p.deleted = false")
    Set<Permission> findByRoleIds(@Param("roleIds") Set<Long> roleIds);

    /**
     * Check if a permission exists by name
     */
    boolean existsByNameAndDeletedFalse(String name);

    /**
     * Find all permissions that match a resource and any of the given actions
     */
    @Query("SELECT p FROM Permission p " +
           "WHERE p.resource = :resource " +
           "AND p.action IN :actions " +
           "AND p.deleted = false")
    List<Permission> findByResourceAndActionIn(@Param("resource") Resource resource, 
                                             @Param("actions") List<Action> actions);

    /**
     * Find permissions by resource and scope
     */
    List<Permission> findByResourceAndScopeAndDeletedFalse(Resource resource, Scope scope);

    /**
     * Find all active permissions ordered by resource then action
     */
    @Query("SELECT p FROM Permission p " +
           "WHERE p.deleted = false " +
           "ORDER BY p.resource, p.action, p.scope")
    List<Permission> findAllActiveOrderedByResourceAndAction();

    /**
     * Count permissions by resource
     */
    @Query("SELECT COUNT(p) FROM Permission p " +
           "WHERE p.resource = :resource AND p.deleted = false")
    long countByResource(@Param("resource") Resource resource);
}