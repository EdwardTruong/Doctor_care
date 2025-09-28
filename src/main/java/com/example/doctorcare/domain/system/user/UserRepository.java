package com.example.doctorcare.domain.system.user;

import java.util.Optional;

import com.example.doctorcare.core.domain.BaseRepository;


/*
 * 1. The findUserByEmail method has to 2 times.
 * 	 	a.It used to find user in UserDetailServiceImple into database (spring security) .
 * 	 	b.It used to check email before register new a user.
 * 
 * 2. The searchingUser method to find user(s).
 */

public interface UserRepository extends BaseRepository<User, Long> {
    Optional<User> findByEmailAndDeletedFalse(String email);
    
    Boolean existsByEmail(String email);

    Optional<User> findByIdAndDeletedFalse(Long id);
    
}
