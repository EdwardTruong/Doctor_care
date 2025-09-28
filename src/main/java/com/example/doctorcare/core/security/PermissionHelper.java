package com.example.doctorcare.core.security;

import com.example.doctorcare.core.enums.Action;
import com.example.doctorcare.core.enums.Resource;
import com.example.doctorcare.core.enums.Scope;
import com.example.doctorcare.domain.system.permission.Permission;

import org.apache.commons.lang3.StringUtils;

/**
 * Lớp tiện ích để xử lý các chuỗi liên quan đến Quyền (Permission).
 * Lớp này đảm bảo tính nhất quán trong cách tạo khóa quyền, tên hiển thị và mô tả.
 */
public final class PermissionHelper {

    private PermissionHelper() {
    }

    /**
     * Xây dựng khóa định danh duy nhất ở cấp hệ thống cho một quyền.
     * Định dạng: ACTION:RESOURCE:SCOPE
     *
     * @param action   Hành động của quyền.
     * @param resource Tài nguyên của quyền.
     * @param scope    Phạm vi của quyền.
     * @return Chuỗi khóa quyền đã được định dạng (ví dụ: "EDIT:PATIENT:OWN").
     */
    public static String buildPermissionKey(Action action, Resource resource, Scope scope) {
        // Ensure scope is never null in the key
        Scope finalScope = (scope == null) ? Scope.ANY : scope;
        return String.format("%s:%s:%s", action.name(), resource.name(), finalScope.name());
    }

    /**
     * Phương thức nạp chồng để xây dựng khóa quyền với scope mặc định là ANY.
     */
    public static String buildPermissionKey(Action action, Resource resource) {
        return buildPermissionKey(action, resource, Scope.ANY);
    }

    /**
     * Tạo tên hiển thị mặc định, thân thiện với người dùng cho một quyền từ các thành phần của nó.
     * Phương thức này sử dụng mô tả từ các enum, có thể được địa phương hóa.
     * Định dạng: "Mô tả Hành động Mô tả Tài nguyên (Mô tả Phạm vi)"
     *
     * @param action   Hành động của quyền.
     * @param resource Tài nguyên của quyền.
     * @param scope    Phạm vi của quyền.
     * @return Tên hiển thị thân thiện với người dùng (ví dụ: "Chỉnh sửa Bệnh án (Bác sỹ)").
     */
    public static String generateDefaultDisplayName(Action action, Resource resource, Scope scope) {
        if (action == null || resource == null) {
            return "";
        }

        String actionDesc = StringUtils.defaultIfBlank(action.getDescription(), action.name());
        String resourceDesc = StringUtils.defaultIfBlank(resource.getDescription(), resource.name());

        if (scope == null || scope == Scope.ANY) {
            return String.format("%s %s", actionDesc, resourceDesc);
        }

        String scopeDesc = StringUtils.defaultIfBlank(scope.getDescription(), scope.name());
        return String.format("%s %s (%s)", actionDesc, resourceDesc, scopeDesc);
    }

    /**
     * Phương thức nạp chồng để tạo tên hiển thị từ một thực thể Permission.
     */
    public static String generateDefaultDisplayName(Permission permission) {
        if (permission == null) {
            return "";
        }
        return generateDefaultDisplayName(permission.getAction(), permission.getResource(), permission.getScope());
    }

    /**
     * Tạo mô tả chi tiết mặc định cho một quyền từ các thành phần của nó.
     *
     * @param action   Hành động của quyền.
     * @param resource Tài nguyên của quyền.
     * @param scope    Phạm vi của quyền.
     * @return Một câu mô tả chi tiết.
     */
    public static String generateDefaultDescription(Action action, Resource resource, Scope scope) {
        if (action == null || resource == null || scope == null) {
            return "Quyền được tạo tự động bởi hệ thống.";
        }
        String actionDesc = StringUtils.defaultIfBlank(action.getDescription(), action.name());
        String resourceDesc = StringUtils.defaultIfBlank(resource.getDescription(), resource.name());
        String scopeDesc = StringUtils.defaultIfBlank(scope.getDescription(), scope.name());

        return String.format("Cho phép %s %s với phạm vi %s.",
                actionDesc.toLowerCase(),
                resourceDesc.toLowerCase(),
                scopeDesc.toLowerCase());
    }

    /**
     * Phương thức nạp chồng để tạo mô tả từ một thực thể Permission.
     */
    public static String generateDefaultDescription(Permission permission) {
        if (permission == null) {
            return "";
        }
        return generateDefaultDescription(permission.getAction(), permission.getResource(), permission.getScope());
    }
}
