package com.example.doctorcare.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.doctorcare.model.entity.Session;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
	Optional<Session> findByKey(String key);
	Optional<Session> findByData(String data);

}
