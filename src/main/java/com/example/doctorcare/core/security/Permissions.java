package com.example.doctorcare.core.security;

import com.example.doctorcare.core.enums.Action;
import com.example.doctorcare.core.enums.Resource;
import com.example.doctorcare.core.enums.Scope;

/**
 * Lớp chứa các hằng số cho các chuỗi quyền (permission strings) trong toàn bộ hệ thống.
 * <p><b>LƯU Ý:</b> File này thường được sinh tự động bởi {@code PermissionClassGenerator}.
 * Không chỉnh sửa file này trực tiếp. Thay vào đó, hãy cập nhật enum {@link Resource}
 * và các annotation của nó, sau đó chạy lại generator.</p>
 */
public final class Permissions {

    private Permissions() {
        // Utility class, prevent instantiation
    }

    /**
     * Các quyền liên quan đến tài nguyên: {@code PERMISSION}
     */
    public static final class Permission {
        public static final String MANAGE_ANY = PermissionHelper.buildPermissionKey(Action.MANAGE, Resource.PERMISSION, Scope.ANY);
        public static final String LIST_ANY   = PermissionHelper.buildPermissionKey(Action.LIST, Resource.PERMISSION, Scope.ANY);
        public static final String VIEW_ANY   = PermissionHelper.buildPermissionKey(Action.VIEW, Resource.PERMISSION, Scope.ANY);
        public static final String EDIT_ANY   = PermissionHelper.buildPermissionKey(Action.EDIT, Resource.PERMISSION, Scope.ANY);

        private Permission() {}
    }

    /**
     * Các quyền liên quan đến tài nguyên: {@code ROLE}
     */
    public static final class Role {
        public static final String MANAGE_ANY = PermissionHelper.buildPermissionKey(Action.MANAGE, Resource.ROLE, Scope.ANY);
        public static final String LIST_ANY   = PermissionHelper.buildPermissionKey(Action.LIST, Resource.ROLE, Scope.ANY);
        public static final String CREATE_ANY = PermissionHelper.buildPermissionKey(Action.CREATE, Resource.ROLE, Scope.ANY);
        public static final String VIEW_ANY   = PermissionHelper.buildPermissionKey(Action.VIEW, Resource.ROLE, Scope.ANY);
        public static final String EDIT_ANY   = PermissionHelper.buildPermissionKey(Action.EDIT, Resource.ROLE, Scope.ANY);
        public static final String DELETE_ANY = PermissionHelper.buildPermissionKey(Action.DELETE, Resource.ROLE, Scope.ANY);

        private Role() {}
    }

    /**
     * Các quyền liên quan đến tài nguyên: {@code ROLE_PERMISSION}
     */
    public static final class RolePermission {
        public static final String ASSIGN_ANY = PermissionHelper.buildPermissionKey(Action.ASSIGN, Resource.ROLE_PERMISSION, Scope.ANY);
        public static final String EDIT_ANY   = PermissionHelper.buildPermissionKey(Action.EDIT, Resource.ROLE_PERMISSION, Scope.ANY);

        private RolePermission() {}
    }

    /**
     * Các quyền liên quan đến tài nguyên: {@code PATIENT}
     */
    public static final class Patient {
        public static final String CREATE_OWN      = PermissionHelper.buildPermissionKey(Action.CREATE, Resource.PATIENT, Scope.OWN);
        public static final String CREATE_ASSIGNED = PermissionHelper.buildPermissionKey(Action.CREATE, Resource.PATIENT, Scope.ASSIGNED);
        public static final String CREATE_MANAGED  = PermissionHelper.buildPermissionKey(Action.CREATE, Resource.PATIENT, Scope.MANAGED);
        public static final String CREATE_ANY      = PermissionHelper.buildPermissionKey(Action.CREATE, Resource.PATIENT, Scope.ANY);

        public static final String VIEW_OWN        = PermissionHelper.buildPermissionKey(Action.VIEW, Resource.PATIENT, Scope.OWN);
        public static final String VIEW_ASSIGNED   = PermissionHelper.buildPermissionKey(Action.VIEW, Resource.PATIENT, Scope.ASSIGNED);
        public static final String VIEW_MANAGED    = PermissionHelper.buildPermissionKey(Action.VIEW, Resource.PATIENT, Scope.MANAGED);
        public static final String VIEW_ANY        = PermissionHelper.buildPermissionKey(Action.VIEW, Resource.PATIENT, Scope.ANY);

        public static final String EDIT_OWN        = PermissionHelper.buildPermissionKey(Action.EDIT, Resource.PATIENT, Scope.OWN);
        public static final String EDIT_ASSIGNED   = PermissionHelper.buildPermissionKey(Action.EDIT, Resource.PATIENT, Scope.ASSIGNED);
        public static final String EDIT_MANAGED    = PermissionHelper.buildPermissionKey(Action.EDIT, Resource.PATIENT, Scope.MANAGED);
        public static final String EDIT_ANY        = PermissionHelper.buildPermissionKey(Action.EDIT, Resource.PATIENT, Scope.ANY);

        public static final String DELETE_OWN      = PermissionHelper.buildPermissionKey(Action.DELETE, Resource.PATIENT, Scope.OWN);
        public static final String DELETE_ASSIGNED = PermissionHelper.buildPermissionKey(Action.DELETE, Resource.PATIENT, Scope.ASSIGNED);
        public static final String DELETE_MANAGED  = PermissionHelper.buildPermissionKey(Action.DELETE, Resource.PATIENT, Scope.MANAGED);
        public static final String DELETE_ANY      = PermissionHelper.buildPermissionKey(Action.DELETE, Resource.PATIENT, Scope.ANY);

        public static final String LIST_OWN        = PermissionHelper.buildPermissionKey(Action.LIST, Resource.PATIENT, Scope.OWN);
        public static final String LIST_ASSIGNED   = PermissionHelper.buildPermissionKey(Action.LIST, Resource.PATIENT, Scope.ASSIGNED);
        public static final String LIST_MANAGED    = PermissionHelper.buildPermissionKey(Action.LIST, Resource.PATIENT, Scope.MANAGED);
        public static final String LIST_ANY        = PermissionHelper.buildPermissionKey(Action.LIST, Resource.PATIENT, Scope.ANY);

        private Patient() {}
    }

    /**
     * Các quyền liên quan đến tài nguyên: {@code USER_PROFILE}
     */
    public static final class UserProfile {
        public static final String VIEW_OWN   = PermissionHelper.buildPermissionKey(Action.VIEW, Resource.USER_PROFILE, Scope.OWN);
        public static final String VIEW_ANY   = PermissionHelper.buildPermissionKey(Action.VIEW, Resource.USER_PROFILE, Scope.ANY);
        public static final String EDIT_OWN   = PermissionHelper.buildPermissionKey(Action.EDIT, Resource.USER_PROFILE, Scope.OWN);
        public static final String EDIT_ANY   = PermissionHelper.buildPermissionKey(Action.EDIT, Resource.USER_PROFILE, Scope.ANY);
        public static final String VERIFY_OWN = PermissionHelper.buildPermissionKey(Action.VERIFY, Resource.USER_PROFILE, Scope.OWN);
        public static final String VERIFY_ANY = PermissionHelper.buildPermissionKey(Action.VERIFY, Resource.USER_PROFILE, Scope.ANY);

        private UserProfile() {}
    }

    /**
     * Các quyền liên quan đến tài nguyên: {@code DATA_VISIBILITY}
     */
    public static final class DataVisibility {
        public static final String MANAGE_ANY   = PermissionHelper.buildPermissionKey(Action.MANAGE, Resource.DATA_VISIBILITY, Scope.ANY);
        public static final String LIST_ANY     = PermissionHelper.buildPermissionKey(Action.LIST, Resource.DATA_VISIBILITY, Scope.ANY);
        public static final String CREATE_ANY   = PermissionHelper.buildPermissionKey(Action.CREATE, Resource.DATA_VISIBILITY, Scope.ANY);
        public static final String VIEW_ANY     = PermissionHelper.buildPermissionKey(Action.VIEW, Resource.DATA_VISIBILITY, Scope.ANY);
        public static final String EDIT_ANY     = PermissionHelper.buildPermissionKey(Action.EDIT, Resource.DATA_VISIBILITY, Scope.ANY);
        public static final String DELETE_ANY   = PermissionHelper.buildPermissionKey(Action.DELETE, Resource.DATA_VISIBILITY, Scope.ANY);

        private DataVisibility() {}
    }

    /**
     * Các quyền liên quan đến tài nguyên: {@code NOTIFICATION}
     */
    public static final class Notification {
        public static final String MANAGE_OWN = PermissionHelper.buildPermissionKey(Action.MANAGE, Resource.NOTIFICATION, Scope.OWN);
        public static final String MANAGE_ANY = PermissionHelper.buildPermissionKey(Action.MANAGE, Resource.NOTIFICATION, Scope.ANY);

        public static final String LIST_OWN   = PermissionHelper.buildPermissionKey(Action.LIST, Resource.NOTIFICATION, Scope.OWN);
        public static final String LIST_ANY   = PermissionHelper.buildPermissionKey(Action.LIST, Resource.NOTIFICATION, Scope.ANY);

        public static final String VIEW_OWN   = PermissionHelper.buildPermissionKey(Action.VIEW, Resource.NOTIFICATION, Scope.OWN);
        public static final String VIEW_ANY   = PermissionHelper.buildPermissionKey(Action.VIEW, Resource.NOTIFICATION, Scope.ANY);

        public static final String CREATE_OWN = PermissionHelper.buildPermissionKey(Action.CREATE, Resource.NOTIFICATION, Scope.OWN);
        public static final String CREATE_ANY = PermissionHelper.buildPermissionKey(Action.CREATE, Resource.NOTIFICATION, Scope.ANY);

        public static final String NOTIFY_OWN = PermissionHelper.buildPermissionKey(Action.NOTIFY, Resource.NOTIFICATION, Scope.OWN);
        public static final String NOTIFY_ANY = PermissionHelper.buildPermissionKey(Action.NOTIFY, Resource.NOTIFICATION, Scope.ANY);

        public static final String DELETE_OWN = PermissionHelper.buildPermissionKey(Action.DELETE, Resource.NOTIFICATION, Scope.OWN);
        public static final String DELETE_ANY = PermissionHelper.buildPermissionKey(Action.DELETE, Resource.NOTIFICATION, Scope.ANY);

        private Notification() {}
    }

    /**
     * Các quyền liên quan đến tài nguyên: {@code SYSTEM_CONFIGURATION}
     */
    public static final class SystemConfiguration {
        public static final String MANAGE_ANY    = PermissionHelper.buildPermissionKey(Action.MANAGE, Resource.SYSTEM_CONFIGURATION, Scope.ANY);
        public static final String VIEW_ANY      = PermissionHelper.buildPermissionKey(Action.VIEW, Resource.SYSTEM_CONFIGURATION, Scope.ANY);
        public static final String EDIT_ANY      = PermissionHelper.buildPermissionKey(Action.EDIT, Resource.SYSTEM_CONFIGURATION, Scope.ANY);
        public static final String CONFIGURE_ANY = PermissionHelper.buildPermissionKey(Action.CONFIGURE, Resource.SYSTEM_CONFIGURATION, Scope.ANY);

        private SystemConfiguration() {}
    }
}