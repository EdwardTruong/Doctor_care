package com.example.doctorcare.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.doctorcare.dao.ScheduleRepository;
import com.example.doctorcare.dto.request.ScheduleInfoRequest;
import com.example.doctorcare.dto.request.ScheduleInfoTestRequest;
import com.example.doctorcare.dto.request.ScheduleRequest;
import com.example.doctorcare.dto.response.ScheduleDtoResponse;
import com.example.doctorcare.entity.DoctorEntity;
import com.example.doctorcare.entity.Schedule;
import com.example.doctorcare.entity.Specializations;
import com.example.doctorcare.entity.UserEntity;
import com.example.doctorcare.exception.RequestException;
import com.example.doctorcare.exception.ScheduleNotFoundException;
import com.example.doctorcare.mapper.ScheduleMapper;
import com.example.doctorcare.service.ScheduleService;
import com.example.doctorcare.service.SpecializationService;
import com.example.doctorcare.utils.ApplicationUtils;
import com.example.doctorcare.utils.Const.MESSENGER_ERROR;
import com.example.doctorcare.utils.Const.MESSENGER_NOT_FOUND;

import jakarta.transaction.Transactional;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	ScheduleRepository sDao;

	@Autowired
	ApplicationUtils appUtils;
	
	@Autowired
	ScheduleMapper scheduleMapper;
	
	@Autowired
	SpecializationService specializationService;
	
	private static final Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);

	
	@Override
	public Schedule findById(Integer id) {
		Optional<Schedule> result = sDao.findById(id);
		return result.orElseThrow(() -> new ScheduleNotFoundException(MESSENGER_NOT_FOUND.SCHEDULE_NOT_FOUND));

	}

	@Override
	public Schedule getScheduleOfDoctorAndDate(DoctorEntity doctorEntity, LocalDate date) {
		Optional<Schedule> result = sDao.findScheduleByDoctor(doctorEntity.getId(), date);
		return result.orElseThrow(() -> new ScheduleNotFoundException(MESSENGER_NOT_FOUND.SCHEDULE_NOT_FOUND));
	}

	@Override
	@Transactional
	public void save(Schedule schedule) {
		sDao.save(schedule);

	}

	@Override
	@Transactional
	public void update(Schedule schedule) {
		sDao.saveAndFlush(schedule);

	}

	@Override
	@Transactional
	public void delete(Schedule schedule) {
		sDao.delete(schedule);

	}

	/*
	 * comment it for quick testing. However, the fake databale to correct value for the time and the maxBooking	
	 */

	@Override
	public Schedule getScheduleOfDoctor(DoctorEntity doctor, Integer idSchedule, String time) {
		Schedule schedule = this.findById(idSchedule);

		logger.info("Booking doctor calling + Doctor name : "+schedule.getDoctorEntity().getUser().getName());
		
		if(schedule.getDoctorEntity().getId() != doctor.getId()) {
			throw new ScheduleNotFoundException(MESSENGER_NOT_FOUND.SCHEDULE_NOT_FOUND);
		}
		
		//comment
		String workHours = schedule.getTime();
		if(!workHours.contains(time)) {
			throw new RuntimeException(MESSENGER_ERROR.TIME_BOOK_ERROR);
		}
		String timeRemaining = this.getSubStringTimeOfSchedule(workHours,time);
		
		schedule.setTime(timeRemaining);
		//comment
		
		schedule.setDoctorEntity(doctor);
		schedule.setMaxBooking(schedule.getMaxBooking()+ "' "+time+" '");
		schedule.setSumBooking(schedule.getSumBooking() + 1);
		this.update(schedule);

		logger.info("Booking doctor calling finish - schedule after update  : " + schedule.toString());

		return schedule;
	}

	@Override
	public Schedule createSchedule(ScheduleRequest request, UserEntity user) {
		DoctorEntity doctor = user.getDoctorEntity();
		
		Specializations specialization = specializationService.findById(request.getSpecializationId());
		
		Schedule schedule = Schedule.builder()
				.createAt(LocalDateTime.now())
				.time(request.getTimes())
				.date(request.getDate())
				.price(request.getPrice())
				.maxBooking("")
				.sumBooking(0)
				.doctorEntity(doctor)
				.specialization(specialization)
				.build();
		this.save(schedule);
		return schedule;
	}

	@Override
	public String getSubStringTimeOfSchedule(String bigTime, String smallTime) {
		Pattern pattern = Pattern.compile(Pattern.quote(smallTime));
		Matcher matcher = pattern.matcher(bigTime);

		if (matcher.find()) {
			int start = matcher.start();
			int end = matcher.end();
			return bigTime.substring(0, start) + bigTime.substring(end);
		} else {
			throw new RuntimeException(MESSENGER_ERROR.TIME_INPUT_ERROR);
		}
	}
	

	@Override
	public List<ScheduleDtoResponse> findScheduleByInfo(ScheduleInfoRequest request) {
    	
		Integer min, max;
		
		if(request.getMinPrice() != null ) {
			 min = Integer.valueOf(request.getMinPrice()) ;
		}else {
			min = 100;
		}
		
		if(request.getMaxPrice() != null) {
			 max = Integer.valueOf(request.getMaxPrice());
		}else {
			max = 10000;			
		}
    	
    	if(min > max) {
    		throw new RequestException(MESSENGER_ERROR.PRICES_ERROR);     		
    	}
    
    	
    	List<Schedule> result = sDao.findScheduleTimeByPriceRangeUsingConcat(request.getSearchString(), request.getDate(),min,max);
    	
    	return scheduleMapper.toBasicListDto(result);
	}
	


	/*
	 * For testing 
	 */
	@Override
	public List<ScheduleDtoResponse> findScheduleByExactlyPrices(ScheduleInfoTestRequest request) {
		List<Schedule> schedules = sDao.findScheduleTimeByPriceRange(request.getPlaceId(), request.getSpecializationId(), request.getClinicId(), request.getPricesList());
		return scheduleMapper.toBasicListDto(schedules);
	}
	
	@Override
	public List<ScheduleDtoResponse> getAllSchedulesByDocId(DoctorEntity doc){
		List<Schedule> schedules = sDao.getSchedulesOfDoctorId(doc.getId());
		return scheduleMapper.toBasicListDto(schedules);

	}

	
}
