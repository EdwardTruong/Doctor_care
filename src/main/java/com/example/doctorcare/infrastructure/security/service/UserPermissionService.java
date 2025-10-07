package com.example.doctorcare.infrastructure.security.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.doctorcare.domain.system.permission.Permission;
import com.example.doctorcare.domain.system.role.model.Role;
import com.example.doctorcare.domain.system.role.repository.RoleRepository;
import com.example.doctorcare.domain.system.user.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Service to handle user permission calculations and role-based access control.
 * This service provides methods to fetch and calculate all permissions for a user
 * based on their roles and role hierarchy.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserPermissionService {

    private final RoleRepository roleRepository;

    /**
     * Calculate all permissions for a user by aggregating permissions from all roles.
     * Includes inherited permissions from parent roles.
     * 
     * @param user The user entity
     * @return Set of all permissions for the user
     */
    public Set<Permission> calculateAllPermissions(User user) {
        Set<Permission> allPermissions = new HashSet<>();
        
        if (user.getUserRoles() != null) {
            for (var userRole : user.getUserRoles()) {
                Role role = userRole.getRole();
                
                // Add permissions from current role
                allPermissions.addAll(getPermissionsFromRole(role));
                
                // Add permissions from parent roles (role hierarchy)
                Role parentRole = role.getParentRole();
                while (parentRole != null) {
                    allPermissions.addAll(getPermissionsFromRole(parentRole));
                    parentRole = parentRole.getParentRole();
                }
            }
        }
        
        log.debug("Calculated {} total permissions for user: {}", 
                allPermissions.size(), user.getAddressEmail());
        
        return allPermissions;
    }
    
    /**
     * Get all permissions directly assigned to a specific role.
     * Fetches permissions from the RolePermission join table.
     * 
     * @param role The role entity
     * @return Set of permissions for the role
     */
    public Set<Permission> getPermissionsFromRole(Role role) {
        Set<Permission> permissions = new HashSet<>();
        
        try {
            // Find the role with permissions eagerly loaded
            Role roleWithPermissions = roleRepository.findByIdWithPermissions(role.getId());
            
            if (roleWithPermissions != null && roleWithPermissions.getRolePermissions() != null) {
                permissions = roleWithPermissions.getRolePermissions()
                    .stream()
                    .map(rolePermission -> rolePermission.getPermission())
                    .collect(HashSet::new, HashSet::add, HashSet::addAll);
            }
            
            log.debug("Found {} permissions for role: {}", 
                    permissions.size(), role.getRoleName());
            
        } catch (Exception e) {
            log.warn("Error fetching permissions for role {}: {}", 
                    role.getRoleName(), e.getMessage());
        }
        
        return permissions;
    }
    
    /**
     * Check if a user has a specific permission based on role assignments.
     * 
     * @param user The user to check
     * @param permissionName The permission name (e.g., "VIEW:PATIENT:OWN")
     * @return true if user has the permission
     */
    public boolean hasPermission(User user, String permissionName) {
        Set<Permission> userPermissions = calculateAllPermissions(user);
        
        return userPermissions.stream()
            .anyMatch(permission -> permissionName.equals(permission.getName()));
    }
    
    /**
     * Check if a user has a specific role.
     * 
     * @param user The user to check
     * @param roleName The role name to check
     * @return true if user has the role
     */
    public boolean hasRole(User user, String roleName) {
        if (user.getUserRoles() == null) return false;
        
        return user.getUserRoles().stream()
            .anyMatch(userRole -> roleName.equals(userRole.getRole().getRoleName()));
    }
}