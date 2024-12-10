package com.example.doctorcare.service.management.impl;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.doctorcare.auth.service.UserService;
import com.example.doctorcare.common.utils.ERole;
import com.example.doctorcare.model.dto.response.UserDtoResponse;
import com.example.doctorcare.model.entity.UserEntity;
import com.example.doctorcare.model.mapper.UserMapper;
import com.example.doctorcare.service.AccountService;
import com.example.doctorcare.service.management.DocterService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DocterServiceImpl implements DocterService {

    static Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

}
