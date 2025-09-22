// package com.example.doctorcare.auth.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import com.example.doctorcare.auth.service.login.JwtResponse;
import com.example.doctorcare.auth.service.login.LoginRequest;
import com.example.doctorcare.model.dto.request.ChangePasswordRequest;
import com.example.doctorcare.model.entity.Session;
import com.example.doctorcare.model.entity.User;

/**
 * Service interface named {@link LoginService} for handling user login
 * operations.
 */
public interface LoginService {

        /**
         * Performs user login based on the provided login request.
         *
         * @param loginRequest The login request containing user credentials.
         * @return The token representing the user's session.
         */

        JwtResponse userLogin(LoginRequest loginRequest)
                        throws io.jsonwebtoken.io.IOException, UnrecoverableKeyException,
                        KeyStoreException, NoSuchAlgorithmException, CertificateException, FileNotFoundException,
                        IOException;

        User changingPassword(ChangePasswordRequest request, Session session)
                        throws io.jsonwebtoken.io.IOException, UnrecoverableKeyException, KeyStoreException,
                        NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException;
 }
