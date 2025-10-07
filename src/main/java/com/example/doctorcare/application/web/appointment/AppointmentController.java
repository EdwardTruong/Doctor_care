package com.example.doctorcare.application.web.appointment;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.doctorcare.application.service.appointment.command.CancelAppointmentCommand;
import com.example.doctorcare.application.service.appointment.command.CreateAppointmentCommand;
import com.example.doctorcare.application.service.appointment.command.UpdateAppointmentCommand;
import com.example.doctorcare.application.service.appointment.dto.AppointmentDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Appointment Management", description = "APIs quản lý lịch hẹn khám")
@RequestMapping("/api/v1/appointments")
public interface AppointmentController {

    @Operation(summary = "Lấy danh sách lịch hẹn với filter và phân trang")
    @GetMapping
    ResponseEntity<Page<AppointmentDto>> getAppointments(
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng items per page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sắp xếp theo field") @RequestParam(defaultValue = "appointmentTime") String sortBy,
            @Parameter(description = "Hướng sắp xếp (asc/desc)") @RequestParam(defaultValue = "desc") String sortDirection,
            @Parameter(description = "ID của bệnh nhân") @RequestParam(required = false) Long patientId,
            @Parameter(description = "ID của bác sĩ") @RequestParam(required = false) Long doctorId,
            @Parameter(description = "ID của phòng khám") @RequestParam(required = false) Long clinicId,
            @Parameter(description = "ID của lịch khám") @RequestParam(required = false) Long scheduleId,
            @Parameter(description = "Trạng thái lịch hẹn") @RequestParam(required = false) String status,
            @Parameter(description = "Ngày hẹn từ (yyyy-MM-dd'T'HH:mm:ss)") @RequestParam(required = false) String appointmentTimeFrom,
            @Parameter(description = "Ngày hẹn đến (yyyy-MM-dd'T'HH:mm:ss)") @RequestParam(required = false) String appointmentTimeTo,
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam(required = false) String keyword
    );

    @Operation(summary = "Lấy danh sách lịch hẹn theo bệnh nhân")
    @GetMapping("/patient/{patientId}")
    ResponseEntity<Page<AppointmentDto>> getAppointmentsByPatient(
            @Parameter(description = "ID của bệnh nhân") @PathVariable Long patientId,
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng items per page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Trạng thái lịch hẹn") @RequestParam(required = false) String status,
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam(required = false) String keyword
    );

    @Operation(summary = "Lấy danh sách lịch hẹn theo bác sĩ")
    @GetMapping("/doctor/{doctorId}")
    ResponseEntity<Page<AppointmentDto>> getAppointmentsByDoctor(
            @Parameter(description = "ID của bác sĩ") @PathVariable Long doctorId,
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng items per page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Trạng thái lịch hẹn") @RequestParam(required = false) String status,
            @Parameter(description = "Ngày hẹn từ (yyyy-MM-dd'T'HH:mm:ss)") @RequestParam(required = false) String appointmentTimeFrom,
            @Parameter(description = "Ngày hẹn đến (yyyy-MM-dd'T'HH:mm:ss)") @RequestParam(required = false) String appointmentTimeTo
    );

    @Operation(summary = "Lấy danh sách lịch hẹn theo phòng khám")
    @GetMapping("/clinic/{clinicId}")
    ResponseEntity<Page<AppointmentDto>> getAppointmentsByClinic(
            @Parameter(description = "ID của phòng khám") @PathVariable Long clinicId,
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng items per page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Trạng thái lịch hẹn") @RequestParam(required = false) String status,
            @Parameter(description = "Ngày hẹn từ (yyyy-MM-dd'T'HH:mm:ss)") @RequestParam(required = false) String appointmentTimeFrom,
            @Parameter(description = "Ngày hẹn đến (yyyy-MM-dd'T'HH:mm:ss)") @RequestParam(required = false) String appointmentTimeTo
    );

    @Operation(summary = "Lấy thông tin chi tiết lịch hẹn")
    @GetMapping("/{id}")
    ResponseEntity<AppointmentDto> getAppointmentDetail(
            @Parameter(description = "ID của lịch hẹn") @PathVariable Long id
    );

    @Operation(summary = "Tạo mới lịch hẹn")
    @PostMapping
    ResponseEntity<Long> createAppointment(
            @Valid @RequestBody CreateAppointmentCommand command
    );

    @Operation(summary = "Cập nhật thông tin lịch hẹn")
    @PutMapping("/{id}")
    ResponseEntity<Void> updateAppointment(
            @Parameter(description = "ID của lịch hẹn") @PathVariable Long id,
            @Valid @RequestBody UpdateAppointmentCommand command
    );

    @Operation(summary = "Hủy lịch hẹn")
    @PutMapping("/{id}/cancel")
    ResponseEntity<Void> cancelAppointment(
            @Parameter(description = "ID của lịch hẹn") @PathVariable Long id,
            @Valid @RequestBody CancelAppointmentCommand command
    );

    @Operation(summary = "Xác nhận hoàn thành lịch hẹn")
    @PutMapping("/{id}/complete")
    ResponseEntity<Void> completeAppointment(
            @Parameter(description = "ID của lịch hẹn") @PathVariable Long id,
            @Parameter(description = "Ghi chú của bác sĩ") @RequestParam(required = false) String doctorNotes,
            @Parameter(description = "Kết quả khám") @RequestParam(required = false) String examinationResult
    );
}