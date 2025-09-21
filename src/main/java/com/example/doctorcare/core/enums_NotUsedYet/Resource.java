package com.example.doctorcare.core.enums_NotUsedYet;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Enum đại diện cho các tài nguyên khác nhau trong hệ thống. Mỗi tài nguyên tương ứng
 * với một thực thể hoặc chức năng cụ thể có thể được quản lý hoặc truy cập trong ứng dụng.
 * <p>
 * Enum này được chú thích với {@link vn.look.core.processor.annotation.GeneratePermissionConstants}
 * để tự động sinh ra lớp hằng số {@code vn.look.core.security.Permissions}.
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

    USER("user", "Người dùng", 
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE, Action.ACTIVATE, Action.DEACTIVATE, Action.RESET_PASSWORD, Action.IMPERSONATE), 
        EnumSet.of(Scope.ANY, Scope.ORGANIZATION, Scope.ENTERPRISE)),

    USER_PROFILE("user_profile", "Hồ sơ người dùng", 
        EnumSet.of(Action.VIEW, Action.EDIT, Action.VERIFY), 
        EnumSet.of(Scope.OWN, Scope.ANY)),

    USER_ROLE("user_role", "Vai trò người dùng", 
        EnumSet.of(Action.ASSIGN, Action.EDIT), 
        EnumSet.of(Scope.ANY, Scope.ORGANIZATION, Scope.ENTERPRISE)),

    DATA_VISIBILITY("data_visibility", "Quy tắc hiển thị dữ liệu", 
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE), 
        EnumSet.of(Scope.ANY)),

    NOTIFICATION("notification", "Thông báo", 
        EnumSet.of(Action.MANAGE, Action.LIST, Action.VIEW, Action.CREATE, Action.NOTIFY, Action.DELETE), 
        EnumSet.of(Scope.OWN, Scope.ANY)),

    SYSTEM_CONFIGURATION("system_configuration", "Cấu hình hệ thống", 
        EnumSet.of(Action.MANAGE, Action.VIEW, Action.EDIT, Action.CONFIGURE), 
        EnumSet.of(Scope.ANY)),

    ADMINISTRATIVE_UNIT("administrative_unit", "Đơn vị hành chính",
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE, Action.IMPORT),
        EnumSet.of(Scope.ANY)),

        
    // Business Domain Resources
    AWARD("award", "Giải thưởng", 
        EnumSet.of(Action.MANAGE, Action.LIST,Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE), 
        EnumSet.of(Scope.ANY)),

    AWARD_INSTANCE("award_instance", "Giải thưởng", 
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE),
        EnumSet.of(Scope.ANY, Scope.OWN, Scope.ORGANIZATION, Scope.ENTERPRISE)),

    BUSINESS_SECTOR("business_sector", "Ngành nghề kinh doanh", 
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE), 
        EnumSet.of(Scope.ANY)),

    BUSINESS_CATEGORY("category", "Lĩnh vực kinh doanh", 
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE), 
        EnumSet.of(Scope.ANY, Scope.ENTERPRISE)),

    CERTIFICATE("certificate", "Chứng nhận",
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE, Action.VERIFY), 
        EnumSet.of(Scope.ANY, Scope.OWN, Scope.ENTERPRISE)),

    POSITION("position", "Chức vụ", 
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE), 
        EnumSet.of(Scope.ANY)),

    ORGANIZATION("organization", "Tổ chức", 
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE, Action.VERIFY),
        EnumSet.of(Scope.ANY, Scope.ORGANIZATION)),

    ENTREPRENEUR("entrepreneur", "Doanh nhân", 
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE), 
        EnumSet.of(Scope.ANY, Scope.OWN, Scope.ORGANIZATION)),

    ACHIEVEMENT("achievement", "Thành tựu", 
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE), 
        EnumSet.of(Scope.ANY, Scope.OWN, Scope.ENTERPRISE)),

    ENTREPRENEUR_DELEGATION("entrepreneur_delegation", "Ủy quyền",
        EnumSet.of(Action.MANAGE, Action.LIST, Action.DELEGATE, Action.VIEW, Action.EDIT, Action.DELETE),
        EnumSet.of(Scope.ANY, Scope.OWN, Scope.ENTERPRISE)),

    /**
     * Quản lý các thành viên (người dùng) được doanh nhân ủy quyền hỗ trợ.
     * Ví dụ: Doanh nhân A ủy quyền cho Nhân viên B.
     * `ENTREPRENEUR_MEMBER` đại diện cho bản ghi liên kết giữa A và B.
     */
    ENTREPRENEUR_MEMBER("entrepreneur_member", "Thành viên ủy quyền doanh nhân",
        EnumSet.of(Action.LIST, Action.VIEW, Action.MANAGE), 
        EnumSet.of(Scope.OWN, Scope.ANY, Scope.ENTERPRISE)),

    ENTERPRISE("enterprise", "Doanh nghiệp", 
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE, Action.VERIFY), 
        EnumSet.of(Scope.ANY, Scope.OWN, Scope.ORGANIZATION, Scope.ENTERPRISE)),

    BUSINESS_PROFILE("business_profile", "Hồ sơ kinh doanh",
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE, Action.APPROVE, Action.REJECT, Action.PUBLISH),
        EnumSet.of(Scope.ANY, Scope.OWN, Scope.ORGANIZATION, Scope.ENTERPRISE)),

    MEMBERSHIP("membership", "Hội viên", 
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE, Action.APPROVE, Action.REJECT, Action.ACTIVATE, Action.DEACTIVATE), 
        EnumSet.of(Scope.ANY, Scope.ORGANIZATION, Scope.OWN)),
    /** 
     * Đại diện cho việc quản lý danh sách thành viên (roster) trong một doanh nghiệp.
     * Ví dụ: thêm một nhân viên mới vào doanh nghiệp, xóa nhân viên khỏi doanh nghiệp.
     * Để quản lý thông tin chi tiết của một người dùng cụ thể trong doanh nghiệp (ví dụ: reset password),
     * hãy sử dụng quyền trên tài nguyên USER với scope ENTERPRISE.
     */
    ENTERPRISE_MEMBER("enterprise_member", "Thành viên Doanh nghiệp",
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE),
        EnumSet.of(Scope.ANY, Scope.OWN, Scope.ENTERPRISE)),

    REFERRAL("referral", "Giới thiệu",
        EnumSet.of(Action.MANAGE, Action.CREATE, Action.VIEW),
        EnumSet.of(Scope.OWN, Scope.ENTERPRISE)
    ),

    // CMS Resources
    CMS("cms", "Nội dung", 
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE), 
        EnumSet.of(Scope.ANY, Scope.OWN)),

    CMS_CATEGORY("cms_category", "Danh mục nội dung", 
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE), 
        EnumSet.of(Scope.ANY, Scope.OWN)),

    CMS_ARTICLE("cms_article", "Bài viết", 
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE, Action.APPROVE, Action.REJECT, Action.SHARE, Action.COMMENT, Action.PUBLISH, Action.UNPUBLISH), 
        EnumSet.of(Scope.ANY, Scope.OWN, Scope.ENTERPRISE)),

    MULTIMEDIA("multimedia", "Đa phương tiện", 
        EnumSet.of(Action.MANAGE, Action.UPLOAD, Action.DOWNLOAD, Action.VIEW, Action.DELETE), 
        EnumSet.of(Scope.ANY, Scope.OWN, Scope.ENTERPRISE)),

    MULTIMEDIA_CATEGORY("multimedia_category", "Danh mục đa phương tiện", 
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE), 
        EnumSet.of(Scope.ANY, Scope.OWN)),

    // Other Resources
    FAQ("faq", "Câu hỏi thường gặp", 
        EnumSet.of(Action.MANAGE, Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE),
        EnumSet.of(Scope.ANY)),
        
    DOCUMENT("document", "Tài liệu",
        EnumSet.of(Action.MANAGE, Action.UPLOAD, Action.DOWNLOAD, Action.VIEW, Action.DELETE),
        EnumSet.of(Scope.ANY, Scope.OWN, Scope.ENTERPRISE)),

    ARTICLE("article", "Tin tức",
            EnumSet.of( Action.LIST, Action.CREATE, Action.VIEW, Action.EDIT, Action.DELETE),
            EnumSet.of(Scope.ANY, Scope.OWN, Scope.ORGANIZATION, Scope.ENTERPRISE))
    ;

    private final String key;
    private final String description;
    private final Set<Action> validActions;
    private final Set<Scope> validScopes;

    Resource(String key, String description, Set<Action> validActions, Set<Scope> validScopes) {
        this.key = key;
        this.description = description;
        this.validActions = Collections.unmodifiableSet(validActions);
        this.validScopes = Collections.unmodifiableSet(validScopes);
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }

    public Set<Action> getValidActions() {
        return validActions;
    }

    public Set<Scope> getValidScopes() {
        return validScopes;
    }

    public boolean isValidAction(Action action) {
        return this.validActions.contains(action);
    }

    public boolean isValidScope(Scope scope) {
        return this.validScopes.contains(scope);
    }

}
