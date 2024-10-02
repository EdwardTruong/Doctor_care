package com.example.doctorcare.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.doctorcare.dao.ClinicsRepository;
import com.example.doctorcare.entity.Clinics;
import com.example.doctorcare.exception.ClinicNotFoundException;
import com.example.doctorcare.service.ClinicsService;
import com.example.doctorcare.utils.Const.MESSENGER_NOT_FOUND;

@Service
public class ClinicsServiceImple implements ClinicsService {

	@Autowired
	ClinicsRepository cDao;
	
	@Override
	public Clinics findById(Integer id) {
		Optional<Clinics> result = cDao.findById(id);
		return result.orElseThrow(()-> new ClinicNotFoundException(MESSENGER_NOT_FOUND.CLINIC_NOTFOUND));
	}

	@Override
	public void save(Clinics clinic) {
		cDao.save(clinic);
		
	}

	@Override
	public void update(Clinics clinic) {
		cDao.saveAndFlush(clinic);
	}

	@Override
	public Clinics findByName(String name) {
		Optional<Clinics> result = cDao.findByName(name);
		return result.orElseThrow(()-> new ClinicNotFoundException(MESSENGER_NOT_FOUND.CLINIC_NOTFOUND));
	}

	@Override
	public List<Clinics> topClinics() {
		return cDao.topClinics();
	}

}
