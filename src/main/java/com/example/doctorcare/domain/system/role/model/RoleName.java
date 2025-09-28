package com.example.doctorcare.domain.system.role.model;

/**
 * Lớp hằng số chứa các tên vai trò (role name) chuẩn trong hệ thống.
 * <p>
 * Việc sử dụng hằng số thay vì chuỗi ký tự hard-code giúp:
 * <ul>
 *     <li>Tránh lỗi chính tả.</li>
 *     <li>Dễ dàng tìm kiếm và tái cấu trúc.</li>
 *     <li>Tăng tính dễ đọc và tường minh của mã nguồn.</li>
 * </ul>
 */
public final class RoleName {

    private RoleName() {}




    public enum Role {
    USER,
    PATIENT,
    DOCTOR,
    MANAGER,
    SUPER_ADMIN
}

    /** Quản trị viên cấp cao nhất, có toàn quyền hệ thống. */
    public static final String SUPER_ADMIN = "SUPER_ADMIN";
    /** Quản trị viên, có quyền quản lý các nghiệp vụ chung trên toàn hệ thống. */
    public static final String ADMIN = "ADMIN";

    /** Vai trò cơ bản nhất của người dùng đã xác thực, không có quyền đặc biệt nào mặc định. */
    public static final String USER = "USER";
    
    /** Quản lý lịch khám cho các bệnh nhân */
    public static final String DOCTOR = "DOCTOR";

    /** Bệnh nhân */
    public static final String PATIENT = "PATIENT";

    /**Cấp quản lý chính của hầu hết hệ thống */
    public static final String MANAGER = "MANAGER";

    // Version 2.0

     /** Vai trò đặc biệt đại diện cho quyền sở hữu một tài nguyên (ví dụ: Doanh nghiệp). */
    public static final String OWNER = "OWNER";

    /**Manager quản lý danh sách của bằng của bác sỹ */
    public static final String PROFILE_REVIEWER = "PROFILE_REVIEWER";

    /** Quản lý nội dung, có quyền tạo, sửa, xóa và xuất bản bài viết, danh mục trên toàn hệ thống. */
    public static final String CONTENT_MANAGER = "CONTENT_MANAGER";

}
