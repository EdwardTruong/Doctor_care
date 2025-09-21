package com.example.doctorcare.application.service.user.mapper;

import org.mapstruct.Mapper;

import com.example.doctorcare.application.service.user.dto.UserSimpleDto;
import com.example.doctorcare.domain.system.user.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    // Giả định User entity có các getter tương ứng.
    // Cần kiểm tra và bổ sung nếu User entity chưa có avatarUrl.
    UserSimpleDto toSimpleDto(User user);

}
