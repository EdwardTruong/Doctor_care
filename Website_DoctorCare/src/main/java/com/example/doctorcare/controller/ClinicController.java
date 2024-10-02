package com.example.doctorcare.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.doctorcare.entity.Clinics;
import com.example.doctorcare.service.ClinicsService;
import com.example.doctorcare.service.ImageService;
import com.example.doctorcare.utils.ApplicationUtils;

import jakarta.validation.Valid;

/*
 * Making later
 */

@RestController
@RequestMapping("api/admin/")
public class ClinicController {

	@Autowired
	ApplicationUtils appUtils;
	
	@Autowired
	ClinicsService cService;
	
	@Autowired
	ImageService imgService;
	
	@PostMapping("/register/clinic")
	public ResponseEntity<Clinics> createNewClinic(@Valid @RequestBody Clinics clinic) {

		clinic.setCreatedAt(LocalDateTime.now());
		cService.save(clinic);
		return ResponseEntity.ok(clinic);
	}
	
	@PutMapping("/update/clinic")
	public ResponseEntity<Clinics> updateClinic(@Valid @RequestBody Clinics clinic) {
		cService.update(clinic);
		return ResponseEntity.ok(clinic);
	}
	
	@PutMapping("/uploadImg/{id}")
	public ResponseEntity<Clinics> uploadImage(@PathVariable Integer idClinic, @RequestParam MultipartFile file){
		
		Clinics entity = cService.findById(idClinic);
		String imgName =imgService.setImageForObject(file);
		
		entity.setImage(imgName);
		
		cService.update(entity);
		
		return ResponseEntity.ok(entity);
		
	}
}
