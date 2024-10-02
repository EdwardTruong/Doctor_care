package com.example.doctorcare.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.doctorcare.dao.SpecializationRepository;
import com.example.doctorcare.entity.Specializations;
import com.example.doctorcare.exception.SpecializationNotFoundException;
import com.example.doctorcare.service.SpecializationService;
import com.example.doctorcare.utils.Const.*;

@Service
public class SpecializationServiceImpl implements SpecializationService {

	@Autowired
	SpecializationRepository sDao;

	@Override
	public Specializations findById(Integer id) {
		Optional<Specializations> result = sDao.findById(id);
		return result
				.orElseThrow(() -> new SpecializationNotFoundException(MESSENGER_NOT_FOUND.SPECIALIZATION_NOTFOUND));
	}

	@Override
	public Specializations findByName(String name) {
		Optional<Specializations> result = sDao.findByName(name);
		return result
				.orElseThrow(() -> new SpecializationNotFoundException(MESSENGER_NOT_FOUND.SPECIALIZATION_NOTFOUND));

	}

	@Override
	public void save(Specializations specialization) {
		sDao.save(specialization);

	}

	@Override
	public void update(Specializations specialization) {
		sDao.saveAndFlush(specialization);

	}

	@Override
	public void delete(Specializations specialization) {
		sDao.delete(specialization);

	}

	@Override
	public List<Specializations> topSpecializations() {
		return sDao.topSpecializations();
	}

	@Override
	public Set<Specializations> findByIds(Set<Integer> ids) {
		Set<Specializations> result = new HashSet<>();
		for (Integer id : ids) {
			Specializations s = this.findById(id);
			result.add(s);
		}
		return result;
	}
	
	@Override
	public String getSpecializationNameFormPatient(Integer patientId) {
		Optional<String> result = sDao.findNameOfSpecialization(patientId);
		return result.orElseThrow(()-> new SpecializationNotFoundException(MESSENGER_NOT_FOUND.SPECIALIZATION_NOTFOUND));
	}

}
