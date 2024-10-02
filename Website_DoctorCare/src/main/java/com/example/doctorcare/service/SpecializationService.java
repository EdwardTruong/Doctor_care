package com.example.doctorcare.service;

import java.util.List;
import java.util.Set;

import com.example.doctorcare.entity.Specializations;

public interface SpecializationService {
	Specializations findByName(String name);

	Specializations findById(Integer name);
	
	void save(Specializations specialization);
	
	void update(Specializations specialization);
	
	void delete(Specializations specialization);

	List<Specializations> topSpecializations();

	Set<Specializations> findByIds(Set<Integer> ids);

	String getSpecializationNameFormPatient(Integer patientId);

}
