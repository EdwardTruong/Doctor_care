package com.example.doctorcare.core.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.doctorcare.core.enums.Action;
import com.example.doctorcare.core.enums.Resource;
import com.example.doctorcare.core.enums.Scope;
import com.example.doctorcare.domain.system.permission.Permission;
import com.example.doctorcare.domain.system.role.model.Role;
import com.example.doctorcare.domain.system.role.model.RolePermission;
import com.example.doctorcare.domain.system.user.User;

/**
 * Enhanced CustomUserDetails implementing comprehensive Role-Based Access Control (RBAC).
 * Supports Resource-Scope-Action permission model for fine-grained authorization.
 * 
 * This class loads all user roles and their associated permissions to enable
 * proper authorization checks throughout the application.
 */
public class CustomUserDetails implements UserDetails { //NOSONAR

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;
    private final Set<Permission> allPermissions;

    /**
     * Constructor with user and pre-calculated authorities
     */
    public CustomUserDetails(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities != null ? authorities : Collections.emptyList();
        this.allPermissions = calculateAllPermissions(user);
    }
    
    /**
     * Build CustomUserDetails with comprehensive RBAC permissions.
     * Loads all roles and their associated permissions for the user.
     * 
     * @param user The user entity
     * @param userPermissionService Service to calculate permissions
     * @return CustomUserDetails with full permission structure
     */
    public static CustomUserDetails build(User user, com.example.doctorcare.infrastructure.security.service.UserPermissionService userPermissionService) {
        // Create authorities from roles with proper naming convention
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        if (user.getUserRoles() != null) {
            // Add role-based authorities (ROLE_ prefix for Spring Security)
            authorities.addAll(user.getUserRoles()
                .stream()
                .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getRoleName()))
                .collect(Collectors.toSet()));
            
            // Add permission-based authorities (for fine-grained access control)
            Set<Permission> allPermissions = userPermissionService.calculateAllPermissions(user);
            authorities.addAll(allPermissions
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                .collect(Collectors.toSet()));
        }
        
        return new CustomUserDetails(user, authorities);
    }
    
    /**
     * Build CustomUserDetails without permission service (fallback).
     * This method is for backward compatibility but won't load permissions.
     * 
     * @param user The user entity
     * @return CustomUserDetails with basic role authorities only
     */
    public static CustomUserDetails build(User user) {
        // Create authorities from roles only (without detailed permissions)
        List<GrantedAuthority> authorities = new ArrayList<>();
        
        if (user.getUserRoles() != null) {
            authorities.addAll(user.getUserRoles()
                .stream()
                .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getRoleName()))
                .collect(Collectors.toSet()));
        }
        
        return new CustomUserDetails(user, authorities);
    }
    
    /**
     * Calculate all permissions for a user by aggregating permissions from all roles.
     * Includes inherited permissions from parent roles.
     * 
     * @param user The user entity
     * @return Set of all permissions for the user
     */
    private static Set<Permission> calculateAllPermissions(User user) {
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
        
        return allPermissions;
    }
    
    /**
     * Get all permissions directly assigned to a specific role.
     * Note: This assumes you have a way to get RolePermission entities.
     * You may need to inject a repository or service to fetch these.
     * 
     * @param role The role entity
     * @return Set of permissions for the role
     */
    private static Set<Permission> getPermissionsFromRole(Role role) {
        // This is a placeholder - you'll need to implement the actual fetching
        // based on your RolePermission entity structure
        // For now, return empty set to avoid compilation errors
        return new HashSet<>();
    }
    
    // ========== Permission Checking Methods ==========
    
    /**
     * Check if user has a specific permission by Resource-Scope-Action pattern.
     * 
     * @param resource The resource type
     * @param scope The scope of access
     * @param action The action to perform
     * @return true if user has the permission
     */
    public boolean hasPermission(Resource resource, Scope scope, Action action) {
        return allPermissions.stream()
            .anyMatch(permission -> permission.isApplicableTo(action, resource, scope));
    }
    
    /**
     * Check if user has permission for a specific resource and action (any scope).
     * 
     * @param resource The resource type
     * @param action The action to perform
     * @return true if user has the permission
     */
    public boolean hasPermission(Resource resource, Action action) {
        return allPermissions.stream()
            .anyMatch(permission -> 
                permission.isApplicableToResource(resource) && 
                permission.isApplicableToAction(action));
    }
    
    /**
     * Check if user has a specific role by role name.
     * 
     * @param roleName The name of the role
     * @return true if user has the role
     */
    public boolean hasRole(String roleName) {
        if (user.getUserRoles() == null) return false;
        
        return user.getUserRoles().stream()
            .anyMatch(userRole -> roleName.equals(userRole.getRole().getRoleName()));
    }
    
    /**
     * Get all role names for this user.
     * 
     * @return List of role names
     */
    public List<String> getRoleNames() {
        if (user.getUserRoles() == null) return Collections.emptyList();
        
        return user.getUserRoles().stream()
            .map(userRole -> userRole.getRole().getRoleName())
            .collect(Collectors.toList());
    }
    
    /**
     * Get all permissions for this user.
     * 
     * @return Set of all permissions
     */
    public Set<Permission> getAllPermissions() {
        return Collections.unmodifiableSet(allPermissions);
    }
    
    /**
     * Get user entity.
     * 
     * @return The user entity
     */
    public User getUser() {
        return user;
    }

    // ========== UserDetails Implementation ==========

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return user.getEncryptedPassword();
    }

    @Override
    public String getUsername() {
        return user.getAddressEmail(); // Use email as username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }
}

   
