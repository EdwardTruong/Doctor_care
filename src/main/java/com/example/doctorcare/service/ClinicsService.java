package com.example.doctorcare.service;

import java.util.List;

import com.example.doctorcare.model.entity.Clinics;
import com.example.doctorcare.service.baseService.CrudService;

public interface ClinicsService extends CrudService<Clinics> {

	Clinics findByName(String name);

	List<Clinics> topClinics();

}
