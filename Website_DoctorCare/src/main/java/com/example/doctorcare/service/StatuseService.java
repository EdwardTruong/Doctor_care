package com.example.doctorcare.service;

import com.example.doctorcare.entity.Statuses;

public interface StatuseService {

	Statuses findById(Integer id);
	Statuses findByName(String name);
	void save(Statuses status);
	void update(Statuses status);
	void delete(Statuses status);
	Statuses createStatus(String nameStatus);
}
