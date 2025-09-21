package com.example.doctorcare.application.service.baseService.old;

import java.util.List;

import com.example.doctorcare.application.service.baseService.CrudService;
import com.example.doctorcare.domain.business.clinics.Clinics;


public interface ClinicsService extends CrudService<Clinics> {

	Clinics findByName(String name);

	List<Clinics> topClinics();

}
