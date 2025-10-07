package com.example.doctorcare.infrastructure.security.domain.login;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.doctorcare.core.cqrs.annotation.CqrsCommandWithResultHandler;
import com.example.doctorcare.core.cqrs.handler.CommandWithResultHandler;
import com.example.doctorcare.core.security.CustomUserDetails;
import com.example.doctorcare.domain.business.session.SessionRepository;
import com.example.doctorcare.infrastructure.security.domain.jwt.JwtUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * CQRS Command Handler for user login functionality.
 * Implements Role-Based Access Control with Resource-Scope-Action permissions.
 * 
 * This handler reuses the existing userLogin logic but follows CQRS pattern
 * and enhances security with comprehensive role and permission management.
 */
@Slf4j
@Service
@CqrsCommandWithResultHandler(LoginCommand.class)
@RequiredArgsConstructor
public class LoginCommandHandler implements CommandWithResultHandler<LoginCommand, LoginSuccessDetailDto> {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final SessionRepository sessionRepository;

    /**
     * Handle user login command - reuses existing userLogin logic
     * with enhanced role-based access control.
     * 
     * @param loginRequest The login command containing email and password
     * @return LoginSuccessDetailDto containing JWT token and user details
     */
    @Override
    public LoginSuccessDetailDto handle(LoginCommand loginRequest) {
        
        log.info("Processing login request for user: {}", loginRequest.email());
        
        // Create authentication token
        UsernamePasswordAuthenticationToken userTryLogin = new UsernamePasswordAuthenticationToken(
            loginRequest.email(), loginRequest.password());
        
        // Authenticate user - this will load CustomUserDetails with full RBAC permissions
        Authentication authentication = authenticationManager.authenticate(userTryLogin);
        
        // Set authentication in security context
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // Generate JWT token with enhanced user details
     
        String jwt;
        try {
            jwt = jwtUtils.generateJwt(authentication);
             // Extract enhanced user details with full role-permission structure
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        
        // Extract role IDs for response (maintaining compatibility)
        List<Integer> roleIds = userDetails.getUser().getUserRoles()
            .stream()
            .map(userRole -> Math.toIntExact(userRole.getRole().getId()))
            .collect(Collectors.toList());
        
        log.info("Login successful for user: {} with roles: {}", 
                userDetails.getUsername(), roleIds);
        
        // Build response with enhanced role information
        return LoginSuccessDetailDto.builder()
            .token(jwt)
            .type("Bearer")
            .id(Math.toIntExact(userDetails.getUser().getId()))
            .email(userDetails.getUsername())
            .rolesId(roleIds)
            .build();
        } catch (UnrecoverableKeyException | KeyStoreException | NoSuchAlgorithmException | CertificateException | IOException | io.jsonwebtoken.io.IOException e) {
            e.printStackTrace();
        } 
        return null;
    }
}
