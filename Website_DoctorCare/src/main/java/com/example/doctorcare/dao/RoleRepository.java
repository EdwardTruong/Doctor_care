package com.example.doctorcare.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.doctorcare.entity.RoleEntity;
import com.example.doctorcare.utils.ERole;

/*
 * I make a method find RoleEntity by name to create new user.
 */

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {
	
	Optional<RoleEntity> findByName (ERole name);
}
