package com.example.doctorcare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.doctorcare.model.entity.UserEntity;



/*
 * 1. The findUserByEmail method has to 2 times.
 * 	 	a.It used to find user in UserDetailServiceImple into database (spring security) .
 * 	 	b.It used to check email before register new a user.
 * 
 * 2. The searchingUser method to find user(s).
 */

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
    Optional<UserEntity> findUserByEmail(String email);
    
    Boolean existsByEmail(String email);
    
}
