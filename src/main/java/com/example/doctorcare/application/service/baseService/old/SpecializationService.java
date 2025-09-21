package com.example.doctorcare.application.service;

import java.util.List;
import java.util.Set;

import com.example.doctorcare.application.service.baseService.CrudService;
import com.example.doctorcare.model.entity.Specializations;

public interface SpecializationService extends CrudService<Specializations> {
	Specializations findByName(String name);

	List<Specializations> topSpecializations();

	Set<Specializations> findByIds(Set<Integer> ids);

	String getSpecializationNameFormPatient(Integer patientId);

}
