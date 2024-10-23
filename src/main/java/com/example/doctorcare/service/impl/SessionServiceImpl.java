package com.example.doctorcare.service.impl;

import java.io.FileNotFoundException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.hibernate.SessionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.doctorcare.dao.SessionRepository;
import com.example.doctorcare.entity.Session;
import com.example.doctorcare.entity.UserEntity;
import com.example.doctorcare.exception.SessionNotFoundException;
import com.example.doctorcare.security.jwt.JwtUtils;
import com.example.doctorcare.service.SessionService;
import com.example.doctorcare.utils.ApplicationUtils;
import com.example.doctorcare.utils.Const.KEY;
import com.example.doctorcare.utils.Const.MESSENGER;
import com.example.doctorcare.utils.Const.MESSENGER_ERROR;
import com.example.doctorcare.utils.Const.MESSENGER_NOT_FOUND;
import com.example.doctorcare.utils.Const.TIME;

import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;

@Service
public class SessionServiceImpl implements SessionService {

	@Autowired
	SessionRepository sDao;

	@Autowired
	ApplicationUtils appUtils;

	@Autowired
	JwtUtils jwtUtils;

	private static final Logger logger = LoggerFactory.getLogger(SessionServiceImpl.class);

	@Override
	@Transactional
	public void save(Session session) {
		sDao.save(session);
	}

	@Override
	@Transactional
	public void update(Session session) {
		sDao.saveAndFlush(session);
	}

	@Override
	@Transactional
	public void delete(Session session) {
		sDao.delete(session);
	}

	@Override
	public Session findById(Integer id) {
		Optional<Session> result = sDao.findById(id);
		return result.orElse(null);
	}

	@Override
	public Session findByKey(String key) {
		Optional<Session> result = sDao.findByKey(key);
		return result.orElse(null);
	}

	@Override
	public Session findByData(String data) {
		Optional<Session> result = sDao.findByData(data);
		return result.orElseThrow(() -> new SessionNotFoundException(MESSENGER_NOT_FOUND.KEY_NOT_FOUND));
	}

	@Override
	@Transactional
	public Session createSessionLogin(String email, String token) {

		String getSessionValue = this.createKey(email);

		Session session = this.findByKey(getSessionValue);
		if (session == null) {
			session = saveNewSession(getSessionValue, token, TIME.EXPIRE_DURATION_JWT_MINUTES);// 6h
		} else {
			session = this.updateSession(session, getSessionValue, token, session.getCreateAt(),
					TIME.EXPIRE_DURATION_JWT_MINUTES);
		}

		logger.info("Calling form appUtils.getCurrentDateTimeBeforeSaveDb() : " + LocalDateTime.now().toString());
		logger.info(MESSENGER.SUCCESS_FUNTION);
		return session;
	}

	public String createKey(String email) {

		if (email.length() >= 36) {
			email = email.substring(0, 32);
		}
		return KEY.VALUE + email;
	}

	@Override
	public String createRandomSessionId(String email) {
		int leftLimit = 97; // letter 'a'
		int rightLimit = 122; // letter 'z'
		int targetStringLength = 36;
		Random random = new Random();

		String generatedString = random.ints(leftLimit, rightLimit + 1).limit(targetStringLength)
				.collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

		return generatedString + email;
	}

	@Override
	public Session getSessionTakeEmailChangePassword(String key) {
		Session session = this.findByKey(key);
		if (session == null) {
			throw new SessionNotFoundException(MESSENGER_NOT_FOUND.SESSION_NOT_FOUND);
		}

		if (session.getCreateAt().isAfter(session.getExpires())) {
			throw new SessionException(MESSENGER_ERROR.SESSION_TIME_OUT);
		}

		return session;
	}


	
	@Override
	public Session sendEmailCreateKeyURl(UserEntity user)
			throws IOException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException,
			CertificateException, FileNotFoundException, java.io.IOException {
		String jwt = jwtUtils.generateTokenFromUsername(user.getEmail());

		String key = this.createKey(user.getEmail());

		String newKey = this.createRandomSessionId(user.getEmail());

		Session currentSession = this.findByKey(key);
		if (currentSession != null) {
			currentSession.setKey(newKey);
			currentSession.setData(jwt);
			currentSession.setUpdateAt(LocalDateTime.now());
			currentSession.setExpires(LocalDateTime.now().plusMinutes(TIME.EXPIRE_DURATION_SESSION_CHANGE_PASSWORD));
			this.update(currentSession);

		} else {
			currentSession = this.saveNewSession(newKey, jwt, TIME.EXPIRE_DURATION_SESSION_CHANGE_PASSWORD);// 15m
		}
		return currentSession;

	}

	@Transactional
	public Session updateSession(Session currentSession, String value, String jwtKey, LocalDateTime dateCreated,
			Long expires) {

		currentSession.setKey(value);
		currentSession.setData(jwtKey);
		currentSession.setUpdateAt(LocalDateTime.now());
		currentSession.setExpires(LocalDateTime.now().plusMinutes(TIME.EXPIRE_DURATION_SESSION_CHANGE_PASSWORD));

		this.update(currentSession);
		return currentSession;
	}

	@Transactional
	@Override
	public Session saveNewSession(String key, String jwtKey, Long expires) {
		Session session = Session.builder()
				.key(key)
				.data(jwtKey)
				.createAt(LocalDateTime.now())
				.expires(LocalDateTime.now().plusMinutes(expires))
				.build();

		this.save(session);
		return session;
	}

}
