package com.example.doctorcare.application.service.baseService.old.impl;
// package com.example.doctorcare.application.service.impl;

// import java.time.LocalDateTime;
// import java.util.Optional;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import com.example.doctorcare.application.exception.StatusException;
// import com.example.doctorcare.application.service.StatuseService;
// import com.example.doctorcare.domain.business.status_schedule.Statuses;
// import com.example.doctorcare.repository.StatusRepository;

// import jakarta.transaction.Transactional;

// @Service
// public class StatusesServiceImpl implements StatuseService {

// 	@Autowired
// 	StatusRepository sDao;

// 	@Override
// 	public Statuses findById(Integer id) {
// 		Optional<Statuses> resutl = sDao.findById(id);
// 		return resutl.orElse(null);
// 	}

// 	@Override
// 	@Transactional
// 	public void save(Statuses status) {
// 		sDao.save(status);

// 	}

// 	@Override
// 	@Transactional
// 	public void update(Statuses status) {
// 		sDao.saveAndFlush(status);

// 	}

// 	@Override
// 	@Transactional
// 	public void delete(Statuses status) {
// 		sDao.delete(status);
// 	}

// 	@Override
// 	public Statuses createStatus(String reason) {
// 		return Statuses.builder().createdAt(LocalDateTime.now()).name(reason).build();

// 	}

// 	@Override
// 	public Statuses findByName(String name) {
// 		Optional<Statuses> result = sDao.findByName(name);
// 		return result.orElseThrow(() -> new StatusException(name));
// 	}

// }
