package com.example.doctorcare.application.web.patient;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.doctorcare.application.service.patient.command.CreatePatientCommand;
import com.example.doctorcare.application.service.patient.command.UpdatePatientCommand;
import com.example.doctorcare.application.service.patient.dto.PatientDto;
import com.example.doctorcare.core.cqrs.utils.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Patient Management", description = "APIs quản lý bệnh nhân")
@RequestMapping("/api/v1/patients")
public interface PatientController {

    @Operation(summary = "Lấy danh sách bệnh nhân với filter và phân trang")
    @GetMapping
    ResponseEntity<Page<PatientDto>> getPatients(
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng items per page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sắp xếp theo field") @RequestParam(defaultValue = "createdAt") String sortBy,
            @Parameter(description = "Hướng sắp xếp (asc/desc)") @RequestParam(defaultValue = "desc") String sortDirection,
            @Parameter(description = "ID của bác sĩ") @RequestParam(required = false) Long doctorId,
            @Parameter(description = "ID của phòng khám") @RequestParam(required = false) Long clinicId,
            @Parameter(description = "Trạng thái bệnh nhân") @RequestParam(required = false) String status,
            @Parameter(description = "Tên bệnh nhân") @RequestParam(required = false) String patientName,
            @Parameter(description = "Email bệnh nhân") @RequestParam(required = false) String patientEmail,
            @Parameter(description = "Số điện thoại bệnh nhân") @RequestParam(required = false) String patientPhone,
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam(required = false) String keyword
    );

    @Operation(summary = "Lấy danh sách bệnh nhân theo bác sĩ")
    @GetMapping("/doctor/{doctorId}")
    ResponseEntity<Page<PatientDto>> getPatientsByDoctor(
            @Parameter(description = "ID của bác sĩ") @PathVariable Long doctorId,
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng items per page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Trạng thái bệnh nhân") @RequestParam(required = false) String status,
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam(required = false) String keyword
    );

    @Operation(summary = "Lấy danh sách bệnh nhân theo phòng khám")
    @GetMapping("/clinic/{clinicId}")
    ResponseEntity<Page<PatientDto>> getPatientsByClinic(
            @Parameter(description = "ID của phòng khám") @PathVariable Long clinicId,
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng items per page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Trạng thái bệnh nhân") @RequestParam(required = false) String status,
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam(required = false) String keyword
    );

    @Operation(summary = "Lấy thông tin chi tiết bệnh nhân")
    @GetMapping("/{id}")
    ResponseEntity<PatientDto> getPatientDetail(
            @Parameter(description = "ID của bệnh nhân") @PathVariable Long id
    );

    @Operation(summary = "Tạo mới bệnh nhân")
    @PostMapping
    ResponseEntity<PatientDto> createPatient(
            @Valid @RequestBody CreatePatientCommand command
    );

    @Operation(summary = "Cập nhật thông tin bệnh nhân")
    @PutMapping("/{id}")
    ResponseEntity<PatientDto> updatePatient(
            @Parameter(description = "ID của bệnh nhân") @PathVariable Long id,
            @Valid @RequestBody UpdatePatientCommand command
    );

    @Operation(summary = "Xóa bệnh nhân (soft delete)")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePatient(
            @Parameter(description = "ID của bệnh nhân") @PathVariable Long id
    );
}