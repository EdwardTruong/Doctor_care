package com.example.doctorcare.application.web.clinic;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.doctorcare.application.service.clinics_.command.CreateClinicCommand;
import com.example.doctorcare.application.service.clinics_.command.UpdateClientCommand;
import com.example.doctorcare.application.service.clinics_.dto.ClinicsDto;
import com.example.doctorcare.core.cqrs.utils.Page;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;


@RequestMapping("/api/v1/clinics")
@Tag(name = "[Admin] Thêm phòng", description = "APIs phòng khám của ông nội supper admin - manager")
public interface ClinicController {


	 @PostMapping
        @Operation(summary = "Thêm mới phòng khám", description = "Thêm phòng khám cho bác sỹ")
        ResponseEntity<ClinicsDto> createNewClinic(
                        @Valid @RequestBody CreateClinicCommand command);

        @PutMapping("/{id}")
        @Operation(summary = "Cập nhật phòng khám", description = "Cập nhật phòng khám")
        ResponseEntity<ClinicsDto> updateClinics(
                        @PathVariable("id") Long clinicId,
                        @Valid @RequestBody UpdateClientCommand request);

        @GetMapping
        @Operation(summary = "Lấy danh phòng khám", description = "Lấy danh phòng khám")
        ResponseEntity<Page<ClinicsDto>> getClinics(
                        @Parameter(description = "Id phòng khám.") @RequestParam(required = false) Long id,
                        @RequestParam(required = false) String keyword,
                        @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable);

        @GetMapping("/{id}")
        @Operation(summary = "Lấy chi tiết phòng khám", description = "Lấy chi tiết phòng khám")
        ResponseEntity<ClinicsDto> getClinic(
                        @Parameter(description = "ID của phòng khám") @PathVariable("id") Long clinicId);                        

        @DeleteMapping("/{id}")
        @Operation(summary = "Xóa phòng khám", description = "Xóa phòng khám")
        ResponseEntity<Void> deleteClinic(
                        @Parameter(description = "ID của phòng khám") @PathVariable("id") Long clinicId);




	
}
