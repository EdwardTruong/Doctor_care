package com.example.doctorcare.auth.service.impl;

import java.io.FileNotFoundException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.doctorcare.auth.security.custom.UserDetailsCustom;
import com.example.doctorcare.auth.service.LoginService;
import com.example.doctorcare.auth.service.UserService;
import com.example.doctorcare.model.dto.request.ChangePasswordRequest;
import com.example.doctorcare.model.dto.request.LoginRequest;
import com.example.doctorcare.model.dto.response.JwtResponse;
import com.example.doctorcare.model.entity.Session;
import com.example.doctorcare.model.entity.UserEntity;
import com.example.doctorcare.service.SessionService;

import io.jsonwebtoken.io.IOException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import com.example.doctorcare.auth.security.jwt.JwtUtils;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginServiceImpl implements LoginService {

        JwtUtils jwtUtils;
        AuthenticationManager authenticationManager;
        UserService userService;
        SessionService sessionService;
        PasswordEncoder encoder;

        @Override
        public JwtResponse userLogin(LoginRequest loginRequest)
                        throws io.jsonwebtoken.io.IOException, UnrecoverableKeyException, KeyStoreException,
                        NoSuchAlgorithmException, CertificateException, IOException, java.io.IOException {
                UsernamePasswordAuthenticationToken userTryLogin = new UsernamePasswordAuthenticationToken(
                                loginRequest.getUsername(), loginRequest.getPassword());
                Authentication authentication = authenticationManager.authenticate(userTryLogin);

                SecurityContextHolder.getContext().setAuthentication(authentication);

                String jwt = jwtUtils.generateJwt(authentication);

                UserDetailsCustom userDetails = (UserDetailsCustom) authentication.getPrincipal();
                List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
                                .collect(Collectors.toList());

                JwtResponse newJwt = JwtResponse.builder().token(jwt).email(userDetails.getUsername()).type("Bearer")
                                .id(userDetails.getId()).roles(roles).build();

                return newJwt;
        }

        @Override
        @Transactional
        public UserEntity changingPassword(ChangePasswordRequest request, Session session)
                        throws io.jsonwebtoken.io.IOException, UnrecoverableKeyException, KeyStoreException,
                        NoSuchAlgorithmException, CertificateException, IOException, java.io.IOException {

                String email = jwtUtils.getUserNameFromJwt(session.getData());

                UserEntity entity = userService.findByEmail(email);
                entity.setPassword(encoder.encode(request.getPassword()));
                entity.setUpdatedAt(LocalDateTime.now());
                userService.update(entity);

                sessionService.delete(session);

                return entity;
        }

}
