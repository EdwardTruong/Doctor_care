package com.example.doctorcare.service;

import com.example.doctorcare.entity.Session;
import com.example.doctorcare.entity.UserEntity;

public interface SessionService {
	
	void save(Session session);

	void update(Session session);
		
	void delete(Session session);
	
	Session saveNewSession(String newSid,String jwtKey, Long expires);
	
	Session createSessionLogin(String email, String token);
	
	Session findById(Integer id);
	
	Session findByKey(String key);
	
	Session findByData(String data);
	
	String createRandomSessionId();
	
	Session getSessionTakeEmailChangePassword(String key);
	
	Session sendEmailCreateKeyURl(UserEntity user);
}
