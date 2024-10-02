package com.example.doctorcare.security.jwt;

import java.io.IOException;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.doctorcare.dto.response.MessageResponse;
import com.example.doctorcare.security.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/*
 * 	This is a layer filter of security. 
 * 	When a user accesses any URLs, this filter is called.
 * 	1. The parseJwt method use to get jwt form HttpServletRequest.
 * 	2. The doFilterInternal method performing : 
 * 		- Getting jwt form HttpServletRequest.
 * 		- if jwt can't get it
 * 		- else :
 * 			- Getting UserDetails(user's infomations was saved when loggin success)  .
 * 			- Using UsernamePasswordAuthenticationToken (this class save a Object principal , Object credentials, Collection<? extends GrantedAuthority> authorities 
 * 				and this constructor should only be used by <code>AuthenticationManager</code>) . 
 * 			 	save userDetails and authorities of user to return type Authentication 		
 * 			- Add more details from HttpServletRequest
 * 			- Save UsernamePasswordAuthenticationToken into SecurityContextHolder. (main purpose)
 * 
 */



public class AuthTokenFilter extends OncePerRequestFilter {
  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String jwt = parseJwt(request);
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());										
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); 

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e);
      
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      response.setContentType("application/json"); 
      ObjectMapper objectMapper = new ObjectMapper();
      String errorMessage = "Unauthorized";
      MessageResponse errorResponse = new MessageResponse(errorMessage);
      String errorResponseJson = objectMapper.writeValueAsString(errorResponse);
      response.getWriter().write(errorResponseJson);
    }

    filterChain.doFilter(request, response);
  }

  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }
    return null;
  }
}

