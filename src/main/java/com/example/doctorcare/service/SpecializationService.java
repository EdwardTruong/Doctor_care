package com.example.doctorcare.service;

import java.util.List;
import java.util.Set;

import com.example.doctorcare.model.entity.Specializations;
import com.example.doctorcare.service.baseService.CrudService;

public interface SpecializationService extends CrudService<Specializations> {
	Specializations findByName(String name);

	List<Specializations> topSpecializations();

	Set<Specializations> findByIds(Set<Integer> ids);

	String getSpecializationNameFormPatient(Integer patientId);

}
