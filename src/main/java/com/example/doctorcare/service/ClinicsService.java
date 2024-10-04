package com.example.doctorcare.service;

import java.util.List;

import com.example.doctorcare.entity.Clinics;

public interface ClinicsService {
	Clinics findById(Integer id);
	
	Clinics findByName(String name);
	
	void save(Clinics clinic);
	
	void update(Clinics clinic);
	
	List<Clinics> topClinics();
}
