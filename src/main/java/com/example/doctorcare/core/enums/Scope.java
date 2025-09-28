package com.example.doctorcare.core.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Enum đại diện cho các phạm vi (scope) khác nhau của quyền hạn trong hệ thống.
 * Scope định nghĩa ngữ cảnh hoặc cấp độ mà một quyền được áp dụng. Nó trả lời
 * cho câu hỏi:
 * "Người dùng có thể thực hiện hành động trên tài nguyên, nhưng là những tài
 * nguyên nào?"
 * <p>
 * Ví dụ: Một quyền có thể chỉ áp dụng cho các tài nguyên mà người dùng sở hữu
 * (OWN),
 * hoặc áp dụng trên toàn hệ thống (ANY).
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Scope {
    /**
     * Phạm vi hẹp nhất: chỉ áp dụng cho dữ liệu mà chính người dùng là chủ sở hữu.
     * Ví dụ: bệnh nhân chỉ xem/sửa bệnh án của chính họ.
     */
    OWN("own", "Sở hữu"),

    /**
     * Phạm vi: áp dụng cho dữ liệu của những đối tượng được gán (assigned).
     * Ví dụ: bác sỹ chỉ được xem bệnh án của các bệnh nhân đã đăng ký khám với mình.
     */
    ASSIGNED("assigned", "Được gán"),

    /**
     * Phạm vi: áp dụng cho dữ liệu thuộc phạm vi quản lý.
     * Ví dụ: manager quản lý bác sỹ thì xem được lịch/bệnh nhân của các bác sỹ đó.
     */
    MANAGED("managed", "Quản lý"),

    /**
     * Phạm vi toàn bộ hệ thống, không giới hạn.
     * Ví dụ: super-admin có toàn quyền với tất cả dữ liệu.
     */
    ANY("any", "Toàn hệ thống"),

    /**
     * Phạm vi dành riêng cho nghiệp vụ hoặc dữ liệu hệ thống, không thuộc về user nào.
     * Ví dụ: config, log, system tasks.
     */
    SYSTEM("system", "Hệ thống");
    
    private final String code;
    private final String description;

    Scope(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static Scope fromKey(String key) {
        for (Scope scope : values()) {
            if (scope.code.equalsIgnoreCase(key))
                return scope;
        }
        return null;
    }

}
