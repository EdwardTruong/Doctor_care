package com.example.doctorcare.application.service.baseService.old.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.doctorcare.domain.system.role.Role;
import com.example.doctorcare.infrastructure.utils.ERole;

/*
 * I make a method find RoleEntity by name to create new user.
 */

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	Optional<Role> findByName (ERole name);
}
