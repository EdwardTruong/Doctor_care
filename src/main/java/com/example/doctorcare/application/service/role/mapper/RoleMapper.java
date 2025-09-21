package com.example.doctorcare.application.service.role.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.doctorcare.application.service.role.dto.RoleDto;
import com.example.doctorcare.domain.system.role.Role;

/**
 * Using mapper to auto mapping user dto -> entity 
 * 
 * @since Sep 10 / 2024 
 * @version 2.0
 */
@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target="parentRoleId", source="parentRole.id")
    @Mapping(target="parentRoleName", source="parentRole.name")    
    RoleDto toDto (Role role);

    @Mapping(target = "parentRole.id", source = "parentRoleId")
    @Mapping(target = "parentRole.name", source = "parentRoleName")
    Role toEntity (RoleDto role);
    
    List<RoleDto> toDtoList(List<Role> roles);
    
    List<Role> toEntityList(List<RoleDto> roleDtos);


}
