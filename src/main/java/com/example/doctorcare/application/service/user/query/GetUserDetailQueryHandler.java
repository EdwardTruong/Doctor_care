package com.example.doctorcare.application.service.user.query;

import org.springframework.stereotype.Service;

import com.example.doctorcare.application.exception.BusinessException;
import com.example.doctorcare.application.service.user.dto.UserDetailDto;
import com.example.doctorcare.core.cqrs.annotation.CqrsQueryHandler;
import com.example.doctorcare.core.cqrs.handler.QueryHandler;
import com.example.doctorcare.domain.system.user.User;
import com.example.doctorcare.domain.system.user.repo.UserRepository;
import com.example.doctorcare.infrastructure.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@CqrsQueryHandler(GetUserDetailQuery.class)
public class GetUserDetailQueryHandler implements QueryHandler<GetUserDetailQuery, UserDetailDto> {

    private final UserRepository userRepository;

    @Override
    public UserDetailDto handle(GetUserDetailQuery query) {
        log.debug("Getting user detail for id: {}", query.userId());
        
        User user = userRepository.findByIdAndDeletedFalse(query.userId())
                .orElseThrow(() -> new BusinessException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "User not found with ID: " + query.userId()
                ));
        
        return UserDetailDto.fromEntity(user);
    }
}