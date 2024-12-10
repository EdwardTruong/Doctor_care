package com.example.doctorcare.controller;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.doctorcare.model.entity.Clinics;
import com.example.doctorcare.service.ClinicsService;
import com.example.doctorcare.service.ImageService;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/*
 * Making later
 */

@RestController
@RequestMapping("api/admin/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClinicController {

	final ClinicsService clinicsService;

	final ImageService imgService;

	@PostMapping("/register/clinic")
	public ResponseEntity<Clinics> createNewClinic(@Valid @RequestBody Clinics clinic) {

		clinic.setCreatedAt(LocalDateTime.now());
		clinicsService.create(clinic);
		return ResponseEntity.ok(clinic);
	}

	@PutMapping("/update/clinic")
	public ResponseEntity<Clinics> updateClinic(@Valid @RequestBody Clinics clinic) {
		clinicsService.update(clinic);
		return ResponseEntity.ok(clinic);
	}

	@PutMapping("/uploadImg/{id}")
	public ResponseEntity<Clinics> uploadImage(@PathVariable Integer idClinic, @RequestParam MultipartFile file) {

		Clinics entity = clinicsService.findById(idClinic);
		String imgName = imgService.setImageForObject(file);

		entity.setImage(imgName);

		clinicsService.update(entity);

		return ResponseEntity.ok(entity);

	}
}
