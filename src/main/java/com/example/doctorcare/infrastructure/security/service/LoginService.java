package com.example.doctorcare.infrastructure.security.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import com.example.doctorcare.domain.business.session.Session;
import com.example.doctorcare.domain.system.user.User;
import com.example.doctorcare.infrastructure.security.old.auth.service.login.ChangePasswordCommand;
import com.example.doctorcare.infrastructure.security.old.auth.service.login.JwtCommand;
import com.example.doctorcare.infrastructure.security.old.auth.service.login.LoginCommand;

/**
 * Service interface named {@link LoginService} for handling user login operations.
 */
public interface LoginService {

        /**
         * Performs user login based on the provided login request.
         *
         * @param loginRequest The login request containing user credentials.
         * @return The token representing the user's session.
         */

        JwtCommand userLogin(LoginCommand loginRequest) throws io.jsonwebtoken.io.IOException,
                        UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException,
                        CertificateException, FileNotFoundException, IOException;

        // User changingPassword(ChangePasswordCommand request, Session session)
        // throws io.jsonwebtoken.io.IOException, UnrecoverableKeyException, KeyStoreException,
        // NoSuchAlgorithmException, CertificateException, FileNotFoundException, IOException;
}
