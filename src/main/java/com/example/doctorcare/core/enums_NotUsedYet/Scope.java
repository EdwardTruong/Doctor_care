package com.example.doctorcare.core.enums_NotUsedYet;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
/**
 * Enum đại diện cho các phạm vi (scope) khác nhau của quyền hạn trong hệ thống.
 * Scope định nghĩa ngữ cảnh hoặc cấp độ mà một quyền được áp dụng. Nó trả lời cho câu hỏi:
 * "Người dùng có thể thực hiện hành động trên tài nguyên, nhưng là những tài nguyên nào?"
 * <p>
 * Ví dụ: Một quyền có thể chỉ áp dụng cho các tài nguyên mà người dùng sở hữu (OWN),
 * hoặc cho tất cả các tài nguyên trong một tổ chức mà họ thuộc về (ORGANIZATION),
 * hoặc áp dụng trên toàn hệ thống (ANY).
 */

/**
 * Nhà đầu tư thì không xem được doanh thu của từng phòng ban, 
 * Nhà đầu tư chỉ xem được tổng chi phí của của 1 quý || năm 
 * 
 * Admin - manager (n) - investors (n) - health center (n) - rooms(n) - Doctor (n) - patient(n) 
 */


@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Scope {
    
     /**
     * Phạm vi hẹp nhất, chỉ áp dụng cho các tài nguyên mà người dùng là chủ sở hữu trực tiếp.
     * Ví dụ: Doanh nhân chỉnh sửa thông tin doanh nghiệp của chính mình.
     */
    OWN("own", "Sở hữu"),

    /**
     * Áp dụng cụ thể cho các tài nguyên liên quan trực tiếp đến một health center đã đầu tư.
     * Để phân biệt các quyền trên gói đầu tư vào health center như một doanh nhân với các quyền
     *      trên hồ sơ của tổ chức mà họ đã tham gia hoặc sở hữu
     */
    INDIVIDUAL("Individual", "Nhà đầu tư cá nhân"),


    /**
     * Áp dụng trong phạm vi nội bộ của một tổ chức đầu tư. Dùng để ủy quyền cho các thành viên trong tổ chức.
     * Ví dụ: Ông to nhất (% cao nhất) có quyền xem ai là người có trong gói đầu tư tổ chức cũng như các doctor có trong 
     * các health center mà tổ chức đó đã đầu tư. 
     */
    INSTITUTIONAL("Institutional", "Nhà đầu tư tổ chức"),


    /**
     * Áp dụng trong phạm vi một phòng ban cụ thể của một doanh nghiệp hoặc tổ chức.
     * Cung cấp mức độ kiểm soát chi tiết hơn so với ENTERPRISE hoặc ORGANIZATION.
     */
    DEPARTMENT("department", "Phòng ban"),

    /**
     * Áp dụng trong phạm vi một tổ chức (ví dụ: hiệp hội).
     * Ví dụ: Quản trị viên của hiệp hội có quyền duyệt hồ sơ của các doanh nghiệp thành viên.
     */
    ORGANIZATION("organization", "Tổ chức"),

    /**
     * Tương tự như ANY, nhưng mang ý nghĩa rõ ràng hơn cho các quyền chỉ dành cho hệ thống,
     * không liên quan đến nghiệp vụ người dùng thông thường.
     */
    SYSTEM("system", "Hệ thống"),


    /**
     * Phạm vi rộng nhất, không bị giới hạn bởi mối quan hệ sở hữu hay thành viên.
     * Thường dành cho quản trị viên cấp cao.
     */
    ANY("any", "Toàn hệ thống");


    final String key;
    final String description;

    Scope(String key, String description) {
        this.key = key;
        this.description = description;
    }

    public static Scope fromKey(String key) {
        for (Scope scope : values()) {
            if (scope.key.equalsIgnoreCase(key)) return scope;
        }
        return null;
    }


}
