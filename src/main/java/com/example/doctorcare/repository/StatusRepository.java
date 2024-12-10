package com.example.doctorcare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.doctorcare.model.entity.Statuses;

@Repository
public interface StatusRepository extends JpaRepository<Statuses, Integer> {
	Optional<Statuses> findByName(String name);
}
