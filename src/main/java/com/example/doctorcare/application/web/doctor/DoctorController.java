package com.example.doctorcare.application.web.doctor;
// package com.example.doctorcare.application.web;

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
import com.example.doctorcare.application.service.doctor_.command.CreateDoctorCommand;
import com.example.doctorcare.application.service.doctor_.command.UpdateDoctorCommand;
import com.example.doctorcare.application.service.doctor_.dto.DoctorDetailDto;
import com.example.doctorcare.application.service.doctor_.dto.DoctorDto;
import com.example.doctorcare.core.cqrs.utils.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

// import java.io.IOException;
// import java.time.LocalDate;
// import java.util.List;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.PutMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.multipart.MultipartFile;

// import com.example.doctorcare.application.service.AccountService;
// import com.example.doctorcare.application.service.DoctorService;
// import com.example.doctorcare.application.service.PatientService;
// import com.example.doctorcare.application.service.ScheduleService;
// import com.example.doctorcare.application.service.StatuseService;
// import com.example.doctorcare.auth.service.UserService;
// import com.example.doctorcare.infrastructure.common.utils.Const.MESSENGER;
// import com.example.doctorcare.model.dto.request.DoctorUpdateRequest;
// import com.example.doctorcare.model.dto.request.ScheduleRequest;
// import com.example.doctorcare.model.dto.response.DoctorDtoResponse;
// import com.example.doctorcare.model.dto.response.PatientDtoDoctorResponse;
// import com.example.doctorcare.model.dto.response.ScheduleDtoResponse;
// import com.example.doctorcare.model.entity.Schedule;
// import com.example.doctorcare.model.entity.User;

// import jakarta.mail.MessagingException;
// import jakarta.validation.Valid;
// import lombok.AccessLevel;
// import lombok.RequiredArgsConstructor;
// import lombok.experimental.FieldDefaults;

@Tag(name = "[Admin] Thêm mới doctor", description = "APIs vùng của ông nội supper admin")
@RequestMapping("/api/v1/doctor")
public interface DoctorController {


	 @PostMapping
        @Operation(summary = "Thêm mới phòng khám", description = "Thêm phòng khám cho bác sỹ")
        ResponseEntity<DoctorDto> createNewDoctor(
                        @Valid @RequestBody CreateDoctorCommand command);

        @PutMapping("/{id}")
        @Operation(summary = "Cập nhật phòng khám", description = "Cập nhật phòng khám")
        ResponseEntity<DoctorDto> updateDoctor(
                        @PathVariable("id") Long clinicId,
                        @Valid @RequestBody UpdateDoctorCommand request);

        @GetMapping
        @Operation(summary = "Lấy danh phòng khám", description = "Lấy danh phòng khám")
        ResponseEntity<Page<DoctorDto>> gerDocs(
                        @Parameter(description = "Id phòng khám.") @RequestParam(required = false) Long id,
                        @RequestParam(required = false) String keyword,
                        @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable);

        @GetMapping("/{id}")
        @Operation(summary = "Lấy chi tiết phòng khám", description = "Lấy chi tiết phòng khám")
        ResponseEntity<DoctorDto> gerDoc(
                        @Parameter(description = "ID của phòng khám") @PathVariable("id") Long clinicId);                        

        @DeleteMapping("/{id}")
        @Operation(summary = "Xóa phòng khám", description = "Xóa phòng khám")
        ResponseEntity<Void> deleteDoc(
                        @Parameter(description = "ID của phòng khám") @PathVariable("id") Long clinicId);




}
    // @PostMapping()
    // ResponseEntity<DoctorDetailDto> createMembership(@Valid @RequestBody CreateDoctorCommand command);

// 	ScheduleService scheduleService;

// 	DoctorService doctorService;

// 	UserService userService;

// 	PatientService patientService;

// 	private static final Logger logger = LoggerFactory.getLogger(DoctorController.class);

// 	/*
// 	 * 5.1.6. Hiển thị thông tin cá nhân: DONE ! Không check nữa !
// 	 */
// 	@GetMapping("/info")
// 	public ResponseEntity<DoctorDtoResponse> showInformation() {
// 		String email = SecurityContextHolder.getContext().getAuthentication().getName();
// 		User entity = userService.findByEmail(email);
// 		DoctorDtoResponse dto = doctorService.getDoctorInfo(entity);
// 		return ResponseEntity.ok(dto);
// 	}

// 	/*
// 	 * 5.2.2. Hiển thị danh sách bệnh nhân DONE ! Không check nữa !
// 	 */
// 	@GetMapping("/patients")
// 	public ResponseEntity<List<PatientDtoDoctorResponse>> getPatientsWithDate(
// 			@RequestParam("date") LocalDate createdAt) {
// 		String email = SecurityContextHolder.getContext().getAuthentication().getName();
// 		List<PatientDtoDoctorResponse> listPatiens = patientService.listPatientsWithDate(email, createdAt);
// 		return new ResponseEntity<>(listPatiens, HttpStatus.OK);
// 	}

// 	/*
// 	 * 5.2.3. Nhận/hủy lịch khám của bệnh nhân DONE ! Không check nữa !
// 	 */
// 	@PutMapping("/changeStatuts/{pId}")
// 	public ResponseEntity<PatientDtoDoctorResponse> changeActivePatients(@PathVariable("pId") Integer patientId,
// 			@RequestParam("status") Integer status, @RequestParam("note") String note) {
// 		String email = SecurityContextHolder.getContext().getAuthentication().getName();
// 		PatientDtoDoctorResponse patient = patientService.changeStatus(patientId, email, status, note);

// 		return new ResponseEntity<>(patient, HttpStatus.OK);
// 	}

// 	@PutMapping("/edit")
// 	public ResponseEntity<DoctorDtoResponse> editUser(@RequestBody DoctorUpdateRequest request) {
// 		String email = SecurityContextHolder.getContext().getAuthentication().getName();
// 		DoctorDtoResponse dto = doctorService.updateDoctor(email, request);
// 		return ResponseEntity.ok(dto);
// 	}

// 	/*
// 	 * 6.1. Gửi thông tin về email cá nhân của bệnh nhân. Maybe not DONE !
// 	 */
// 	@PostMapping("/send-result")
// 	public ResponseEntity<String> sendReultToPatien(@RequestParam("toMail") String toMail,
// 			@RequestParam("file") MultipartFile file) throws MessagingException, IOException {
// 		String email = SecurityContextHolder.getContext().getAuthentication().getName();
// 		User toUser = userService.findByEmail(toMail);
// 		String result = doctorService.doctorSendEmail(email, toUser, file);
// 		return ResponseEntity.ok(result);
// 	}

// 	/*
// 	 * For testing
// 	 */
// 	@PostMapping("/create-schedule")
// 	public ResponseEntity<Schedule> createSchedule(@Valid @RequestBody ScheduleRequest request) {
// 		String email = SecurityContextHolder.getContext().getAuthentication().getName();
// 		User user = userService.findByEmail(email);
// 		Schedule createSchedule = scheduleService.createSchedule(request, user);
// 		logger.info(MESSENGER.SUCCESS + createSchedule.toString());
// 		return new ResponseEntity<>(createSchedule, HttpStatus.OK);
// 	}

// 	/*
// 	 * For testing
// 	 */
// 	@GetMapping("/get-schedules")
// 	public ResponseEntity<List<ScheduleDtoResponse>> docSchedules() {
// 		String email = SecurityContextHolder.getContext().getAuthentication().getName();
// 		User user = userService.findByEmail(email);
// 		List<ScheduleDtoResponse> result = scheduleService.getAllSchedulesByDocId(user.getDoctor());
// 		return ResponseEntity.ok(result);
	// }
// }
