// package com.example.doctorcare.application.service.baseService.old;

// import java.io.IOException;
// import java.time.LocalDate;
// import java.util.List;
// import java.util.Set;

// import org.springframework.web.multipart.MultipartFile;

// import com.example.doctorcare.domain.system.user.User;


// import jakarta.mail.MessagingException;

// /*
//  * 
//  * 
//  * The getListDoctor method use to get all doctor of a patient. 
//  * 	This function will be making later.
//  */

// public interface DoctorService {
// 	Doctor findById(Integer idDoctor);

// 	void save(Doctor docter);

// 	void update(Doctor docter);

// 	DoctorDtoResponse createNewDoctor(SignupDoctorRequest request, User user,
// 			Set<Specializations> specializations, Clinics clinic);

// 	List<DoctorDto> getlistDocSaveSpecializationWithSchedule(String nameSpecialization, LocalDate date);

// 	DoctorDtoResponse getDoctorInfo(User entity);

// 	DoctorWithSchedulesResponse getDoctorDtoWithScheduleDtoForAdmin(Doctor doctor);

// 	DoctorDtoResponse lockDoc(Integer id, String reason);
	
// 	DoctorDtoResponse unlockDoc(Integer id, String reason);

// 	DoctorDtoResponse updateDoctor(String email, DoctorUpdateRequest request);

// 	String doctorSendEmail(String docEmail , User toUser, MultipartFile file)
// 			throws MessagingException, IOException;

// 	// Later
// 	List<Doctor> getListDoctor(List<Integer> idsDoctor);
// }
