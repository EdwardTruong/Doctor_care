package com.example.doctorcare.core.enums_NotUsedYet;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Enum representing various actions that can be performed on resources.
 * Each action corresponds to a specific operation that can be executed
 * within the application, such as reading, creating, deleting, updating, or executing.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Action {
    
    MANAGE("manage", "Quản lý"),
    LIST("list", "Xem danh sách"), 
    VIEW("view", "Xem chi tiết"),
    CREATE("create", "Tạo mới"),
    EDIT("edit", "Chỉnh sửa"),
    DELETE("delete", "Xoá"),
    EXECUTE("execute", "Thực thi"),
    REVIEW("review", "Đánh giá"),
    APPROVE("approve", "Phê duyệt"),
    REJECT("reject", "Từ chối"),
    PUBLISH("publish", "Xuất bản"),
    UNPUBLISH("unpublish", "Gỡ xuất bản"),
    DELEGATE("delegate", "Ủy quyền"),
    ASSIGN("assign", "Gán"),
    ACTIVATE("activate", "Kích hoạt"),
    DEACTIVATE("deactivate", "Vô hiệu hoá"),
    REQUEST("request", "Yêu cầu"),
    RESPOND("respond", "Phản hồi"),
    LIKE("like", "Thích"),
    SHARE("share", "Chia sẻ"),
    COMMENT("comment", "Bình luận"),
    REPLY("reply", "Trả lời"),
    SUBSCRIBE("subscribe", "Đăng ký"),
    UNSUBSCRIBE("unsubscribe", "Hủy đăng ký"),
    FOLLOW("follow", "Theo dõi"),
    UNFOLLOW("unfollow", "Hủy theo dõi"),
    SEARCH("search", "Tìm kiếm"),
    RECOMMEND("recommend", "Đề xuất"),
    USE_AI("use_ai", "Sử dụng AI"),
    EXPORT("export", "Xuất dữ liệu"),
    IMPORT("import", "Nhập dữ liệu"),
    BACKUP("backup", "Lưu trữ"),
    RESTORE("restore", "Khôi phục"),
    SYNCHRONIZE("synchronize", "Đồng bộ hóa"),
    UPLOAD("upload", "Tải lên"),
    DOWNLOAD("download", "Tải xuống"),
    NOTIFY("notify", "Thông báo"),
    VERIFY("verify", "Xác minh"),
    VALIDATE("validate", "Xác thực dữ liệu"),
    ARCHIVE("archive", "Lưu trữ"),
    ANALYZE("analyze", "Phân tích"),
    MONITOR("monitor", "Giám sát"),
    REPORT("report", "Báo cáo"),
    CONFIGURE("configure", "Cấu hình"),
    RESET_PASSWORD("reset_password", "Đặt lại mật khẩu"),
    IMPERSONATE("impersonate", "Mạo danh"),
    // Dùng cho các hành động tùy chỉnh không phù hợp với các hành động tiêu chuẩn.
    // Hạn chế sử dụng và nên định nghĩa action cụ thể nếu có thể.
    CUSTOM("custom", "Tùy chỉnh")
    ; 

    private final String key;
    private final String description;

    Action(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public String getKey() {
        return key;
    }

    public String getDescription() {
        return description;
    }
}
