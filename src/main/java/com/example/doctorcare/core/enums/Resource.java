package com.example.doctorcare.core.enums;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;

/**
 * Enum đại diện cho các tài nguyên khác nhau trong hệ thống. Mỗi tài nguyên
 * tương ứng
 * với một thực thể hoặc chức năng cụ thể có thể được quản lý hoặc truy cập
 * trong ứng dụng.
 * <p>
 * Enum này được chú thích với
 * {@ com.example.doctorcare.core.processor.annotation..GeneratePermissionConstants}
 * để tự động sinh ra lớp hằng số
 * {@code com.example.doctorcare.core.security.Permissions}.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Resource {
    // System & Admin Resources
    PERMISSION("permission", "Quyền",
            EnumSet.of(Action.MANAGE, Action.LIST, Action.VIEW, Action.EDIT),
            EnumSet.of(Scope.ANY)),

    ROLE("role", "Vai trò",
            EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE),
            EnumSet.of(Scope.ANY)),

    ROLE_PERMISSION("role_permission", "Quyền vai trò",
            EnumSet.of(Action.ASSIGN, Action.EDIT),
            EnumSet.of(Scope.ANY)),

    PATIENT("patient", "Bệnh nhân",
            EnumSet.of(Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE, Action.LIST),
            EnumSet.of(Scope.OWN, Scope.ASSIGNED, Scope.MANAGED, Scope.ANY)),

    USER_PROFILE("user_profile", "Thông tin của người dùng",
            EnumSet.of(Action.VIEW, Action.EDIT, Action.VERIFY),
            EnumSet.of(Scope.OWN, Scope.ANY)),

    DATA_VISIBILITY("data_visibility", "Quy tắc hiển thị dữ liệu",
            EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE),
            EnumSet.of(Scope.ANY)),

    NOTIFICATION("notification", "Thông báo",
            EnumSet.of(Action.MANAGE, Action.LIST, Action.VIEW, Action.CREATE, Action.NOTIFY, Action.DELETE),
            EnumSet.of(Scope.OWN, Scope.ANY)),

    SYSTEM_CONFIGURATION("system_configuration", "Cấu hình hệ thống",
            EnumSet.of(Action.MANAGE, Action.VIEW, Action.EDIT, Action.CONFIGURE),
            EnumSet.of(Scope.ANY));

    @Getter
    private final String key;
    @Getter
    private final String description;
    @Getter
    private final Set<Action> validActions;
    @Getter
    private final Set<Scope> validScopes;

    Resource(String key, String description, Set<Action> validActions, Set<Scope> validScopes) {
        this.key = key;
        this.description = description;
        this.validActions = Collections.unmodifiableSet(validActions);
        this.validScopes = Collections.unmodifiableSet(validScopes);
    }

    public boolean isValidAction(Action action) {
        return this.validActions.contains(action);
    }

    public boolean isValidScope(Scope scope) {
        return this.validScopes.contains(scope);
    }
}
