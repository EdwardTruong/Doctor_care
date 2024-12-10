package com.example.doctorcare.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.doctorcare.common.utils.Const.*;
import com.example.doctorcare.exception.notfound.SpecializationNotFoundException;
import com.example.doctorcare.model.entity.Specializations;
import com.example.doctorcare.repository.SpecializationRepository;
import com.example.doctorcare.service.SpecializationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpecializationServiceImpl implements SpecializationService {

	private final SpecializationRepository specializationRepository;

	@Override
	public Specializations findById(Integer id) {
		Optional<Specializations> result = specializationRepository.findById(id);
		return result
				.orElseThrow(() -> new SpecializationNotFoundException(MESSENGER_NOT_FOUND.SPECIALIZATION_NOTFOUND));
	}

	@Override
	public Specializations findByName(String name) {
		Optional<Specializations> result = specializationRepository.findByName(name);
		return result
				.orElseThrow(() -> new SpecializationNotFoundException(MESSENGER_NOT_FOUND.SPECIALIZATION_NOTFOUND));

	}

	@Override
	public void create(Specializations specialization) {
		specializationRepository.save(specialization);

	}

	@Override
	public void delete(Specializations specialization) {
		specializationRepository.delete(specialization);

	}

	@Override
	public List<Specializations> topSpecializations() {
		return specializationRepository.topSpecializations();
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
		Optional<String> result = specializationRepository.findNameOfSpecialization(patientId);
		return result
				.orElseThrow(() -> new SpecializationNotFoundException(MESSENGER_NOT_FOUND.SPECIALIZATION_NOTFOUND));
	}

	@Override
	public Specializations update(Specializations specialization) {
		return specializationRepository.saveAndFlush(specialization);
	}

	@Override
	public List<Specializations> findAll() {
		return specializationRepository.findAll();
	}

}
