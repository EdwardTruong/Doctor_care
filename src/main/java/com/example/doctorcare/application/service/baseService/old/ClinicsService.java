package com.example.doctorcare.application.service;

import java.util.List;

import com.example.doctorcare.application.service.baseService.CrudService;
import com.example.doctorcare.model.entity.Clinics;

public interface ClinicsService extends CrudService<Clinics> {

	Clinics findByName(String name);

	List<Clinics> topClinics();

}
