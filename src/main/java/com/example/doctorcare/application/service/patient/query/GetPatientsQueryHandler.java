package com.example.doctorcare.application.service.patient.query;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.example.doctorcare.application.service.patient.dto.PatientDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsPageQueryHandler;
import com.example.doctorcare.core.cqrs.handler.PageQueryHandler;
import com.example.doctorcare.core.cqrs.utils.Paging;
import com.example.doctorcare.domain.business.patients.PatientRepository;
import com.example.doctorcare.domain.business.patients.Patients;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsPageQueryHandler(GetPatientsQuery.class)
public class GetPatientsQueryHandler implements PageQueryHandler<GetPatientsQuery, PatientDto> {

    private final PatientRepository patientRepository;

    @Override
    public com.example.doctorcare.core.cqrs.utils.Page<PatientDto> handle(GetPatientsQuery query) {
        log.debug("Getting patients with filters - doctorId: {}, clinicId: {}, status: {}", 
                 query.doctorId(), query.clinicId(), query.status());

        Page<Patients> patientsPage;
        
        // Apply filters theo yêu cầu: Doctor ID, Clinic ID hoặc cả hai
        if (query.hasBothDoctorAndClinicFilter()) {
            // Phân trang theo cả Doctor và Clinic
            patientsPage = patientRepository.findByDoctorIdAndClinicId(
                query.doctorId(), query.clinicId(), query.pageable());
        } else if (query.hasDoctorFilter()) {
            // Phân trang theo Doctor ID
            if (query.status() != null) {
                patientsPage = patientRepository.findByDoctorIdAndStatus(
                    query.doctorId(), query.status(), query.pageable());
            } else {
                patientsPage = patientRepository.findByDoctorId(
                    query.doctorId(), query.pageable());
            }
        } else if (query.hasClinicFilter()) {
            // Phân trang theo Clinic ID
            patientsPage = patientRepository.findByClinicId(
                query.clinicId(), query.pageable());
        } else if (query.patientName() != null && !query.patientName().trim().isEmpty()) {
            // Search theo tên patient
            patientsPage = patientRepository.findByPatientNameContainingIgnoreCase(
                query.patientName(), query.pageable());
        } else if (query.status() != null) {
            // Filter theo status
            patientsPage = patientRepository.findByStatusAndDeletedFalse(
                query.status(), query.pageable());
        } else {
            // Lấy tất cả patients (default)
            patientsPage = patientRepository.findByDeletedFalse(query.pageable());
        }

        // Convert to DTOs
        Page<PatientDto> dtoPage = patientsPage.map(PatientDto::fromEntityMinimal);

        log.debug("Found {} patients", dtoPage.getTotalElements());

        return Paging.of(dtoPage);
    }
}