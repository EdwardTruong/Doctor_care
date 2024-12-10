package com.example.doctorcare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.doctorcare.common.utils.ERole;
import com.example.doctorcare.model.entity.RoleEntity;

/*
 * I make a method find RoleEntity by name to create new user.
 */

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Integer> {

	Optional<RoleEntity> findByName(ERole name);
}
