package com.example.doctorcare.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.doctorcare.common.utils.Const.MESSENGER_NOT_FOUND;
import com.example.doctorcare.exception.notfound.ClinicNotFoundException;
import com.example.doctorcare.model.entity.Clinics;
import com.example.doctorcare.repository.ClinicsRepository;
import com.example.doctorcare.service.ClinicsService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClinicsServiceImple implements ClinicsService {

	private final ClinicsRepository clinicsRepository;

	@Override
	public void create(Clinics clinics) {
		clinicsRepository.save(clinics);
	}

	/**
	 * Finds a clinic by its unique identifier.
	 *
	 * @param id the unique identifier of the clinic to find
	 * @return the Clinics object if found
	 * @throws ClinicNotFoundException if no clinic is found with the given id
	 */
	@Override
	public Clinics findById(Integer id) {
		Optional<Clinics> result = clinicsRepository.findById(id);
		return result.orElseThrow(() -> new ClinicNotFoundException(MESSENGER_NOT_FOUND.CLINIC_NOTFOUND));
	}

	/**
	 * Updates a clinic.
	 *
	 * @param clinic the clinic to update
	 * @return the updated clinic
	 */
	@Override
	public Clinics update(Clinics clinic) {
		return clinicsRepository.saveAndFlush(clinic);
	}

	/**
	 * Deletes a clinic.
	 *
	 * @param clinic the clinic to delete
	 */
	@Override
	public void delete(Clinics clinic) {
		clinicsRepository.delete(clinic);

	}

	@Override
	public List<Clinics> findAll() {
		return null;
	}

	/**
	 * Returns a list of top clinics, sorted by the number of patients they have
	 * and the number of views they have received.
	 *
	 * @return a list of the top 3 clinics
	 */
	@Override
	public List<Clinics> topClinics() {
		return clinicsRepository.findTop3ByOrderByPatients_IdCountDescAndViewDesc();
	}

	/**
	 * Finds a clinic by its name.
	 *
	 * @param name the name of the clinic to find
	 * @return the Clinics object if found
	 * @throws ClinicNotFoundException if no clinic is found with the given name
	 */
	@Override
	public Clinics findByName(String name) {
		Optional<Clinics> result = clinicsRepository.findByName(name);
		return result.orElseThrow(
				() -> new ClinicNotFoundException(MESSENGER_NOT_FOUND.CLINIC_NOTFOUND + ": '" + name + "'."));
	}

}
