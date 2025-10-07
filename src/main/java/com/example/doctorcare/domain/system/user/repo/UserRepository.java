package com.example.doctorcare.domain.system.user.repo;

import java.util.Optional;

import com.example.doctorcare.core.domain.BaseRepository;
import com.example.doctorcare.domain.system.user.User;


/*
 * 1. The findUserByEmail method has to 2 times.
 * 	 	a.It used to find user in UserDetailServiceImple into database (spring security) .
 * 	 	b.It used to check email before register new a user.
 * 
 * 2. The searchingUser method to find user(s).
 */

public interface UserRepository extends BaseRepository<User, Long> {
    Optional<User> findByEmailAndDeleted(String email, boolean deleted);
    
    Boolean existsByEmail(String email);

    Optional<User> findByIdAndDeletedFalse(Long id);
    
}
