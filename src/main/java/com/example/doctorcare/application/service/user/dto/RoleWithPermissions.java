package com.example.doctorcare.application.service.user.dto;


import java.io.Serializable;
import java.util.Set;

import lombok.Builder;
import lombok.Value;

/**
 * DTO chứa thông tin về một vai trò và tập hợp các chuỗi quyền (đã bao gồm kế thừa) mà vai trò đó cấp.
 */
@Value
@Builder
public class RoleWithPermissions implements Serializable {
    Long roleId;
    String roleName;
    String roleDescription;
    String position;
    String contextId;
    Set<String> permissions;
}
