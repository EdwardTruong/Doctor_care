package com.example.doctorcare.application.service.user.dto;

import java.util.List;


import com.example.doctorcare.application.service.role.dto.RoleDto;
import com.example.doctorcare.core.domain.BaseDto;
import com.example.doctorcare.core.enums_NotUsedYet.Gender;
import com.example.doctorcare.domain.system.user.UserType;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@Builder
@EqualsAndHashCode(callSuper = true)
public class UserDto extends BaseDto<Long> {

    private Long id;
    private String uuid;
    private String username;
    private String email;
    private String phoneNumber;
    private Long avatarId;
    private String avatarUrl;
    private String fullName;
    private String address;
    private Gender gender;
    private boolean active;
    private UserType userType;
    private List<RoleDto> roles;


}
