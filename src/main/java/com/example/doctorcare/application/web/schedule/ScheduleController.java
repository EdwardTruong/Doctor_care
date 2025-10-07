package com.example.doctorcare.application.web.schedule;

import com.example.doctorcare.core.cqrs.utils.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.doctorcare.application.service.schedule.command.CreateScheduleCommand;
import com.example.doctorcare.application.service.schedule.command.UpdateScheduleCommand;
import com.example.doctorcare.application.service.schedule.dto.ScheduleDto;
import com.example.doctorcare.application.service.schedule.query.GetScheduleDetailQuery;
import com.example.doctorcare.application.service.schedule.query.GetSchedulesQuery;
import com.example.doctorcare.application.service.schedule.query.GetSchedulesByDoctorQuery;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Schedule Management", description = "APIs quản lý lịch khám của bác sĩ")
@RequestMapping("/api/v1/schedules")
public interface ScheduleController {

    @Operation(summary = "Lấy danh sách lịch khám với filter và phân trang")
    @GetMapping
    ResponseEntity<Page<ScheduleDto>> getSchedules(
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng items per page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Sắp xếp theo field") @RequestParam(defaultValue = "date") String sortBy,
            @Parameter(description = "Hướng sắp xếp (asc/desc)") @RequestParam(defaultValue = "asc") String sortDirection,
            @Parameter(description = "ID của bác sĩ") @RequestParam(required = false) Long doctorId,
            @Parameter(description = "ID của chuyên khoa") @RequestParam(required = false) Long specializationId,
            @Parameter(description = "Ngày từ (yyyy-MM-dd)") @RequestParam(required = false) String dateFrom,
            @Parameter(description = "Ngày đến (yyyy-MM-dd)") @RequestParam(required = false) String dateTo,
            @Parameter(description = "Giá tối thiểu") @RequestParam(required = false) Double minPrice,
            @Parameter(description = "Giá tối đa") @RequestParam(required = false) Double maxPrice,
            @Parameter(description = "Từ khóa tìm kiếm") @RequestParam(required = false) String keyword,
            @Parameter(description = "Chỉ lấy lịch có slot trống") @RequestParam(defaultValue = "false") boolean availableOnly
    );

    @Operation(summary = "Lấy danh sách lịch khám theo bác sĩ")
    @GetMapping("/doctor/{doctorId}")
    ResponseEntity<Page<ScheduleDto>> getSchedulesByDoctor(
            @Parameter(description = "ID của bác sĩ") @PathVariable Long doctorId,
            @Parameter(description = "Số trang (bắt đầu từ 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số lượng items per page") @RequestParam(defaultValue = "20") int size,
            @Parameter(description = "Ngày từ (yyyy-MM-dd)") @RequestParam(required = false) String dateFrom,
            @Parameter(description = "Ngày đến (yyyy-MM-dd)") @RequestParam(required = false) String dateTo,
            @Parameter(description = "Chỉ lấy lịch có slot trống") @RequestParam(defaultValue = "false") boolean availableOnly
    );

    @Operation(summary = "Lấy thông tin chi tiết lịch khám")
    @GetMapping("/{id}")
    ResponseEntity<ScheduleDto> getScheduleDetail(
            @Parameter(description = "ID của lịch khám") @PathVariable Long id
    );

    @Operation(summary = "Tạo mới lịch khám")
    @PostMapping
    ResponseEntity<ScheduleDto> createSchedule(
            @Valid @RequestBody CreateScheduleCommand command
    );

    @Operation(summary = "Cập nhật thông tin lịch khám")
    @PutMapping("/{id}")
    ResponseEntity<ScheduleDto> updateSchedule(
            @Parameter(description = "ID của lịch khám") @PathVariable Long id,
            @Valid @RequestBody UpdateScheduleCommand command
    );

    @Operation(summary = "Xóa lịch khám (soft delete)")
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteSchedule(
            @Parameter(description = "ID của lịch khám") @PathVariable Long id
    );
}