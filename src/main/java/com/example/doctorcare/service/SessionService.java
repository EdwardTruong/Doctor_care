package com.example.doctorcare.service;

import java.io.FileNotFoundException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import com.example.doctorcare.model.entity.Session;
import com.example.doctorcare.model.entity.UserEntity;

import io.jsonwebtoken.io.IOException;

public interface SessionService {

	void save(Session session);

	void update(Session session);

	void delete(Session session);

	Session saveNewSession(String newSid, String jwtKey, Long expires);

	Session createSessionLogin(String email, String token);

	Session findById(Integer id);

	Session findByKey(String key);

	Session findByData(String data);

	String createRandomSessionId(String email);

	Session getSessionTakeEmailChangePassword(String key);

	Session sendEmailCreateKeyURl(UserEntity user) throws IOException, UnrecoverableKeyException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException, FileNotFoundException, java.io.IOException;
}
