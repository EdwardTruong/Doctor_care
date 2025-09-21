package com.example.doctorcare.core.domain;

import java.io.Serializable;

/**
 * Một interface đánh dấu (marker interface) và cung cấp hợp đồng cho các thực thể domain
 * có thể là đối tượng của việc kiểm tra quyền trong {@link vn.look.infrastructure.security.CustomPermissionEvaluator}.
 * <p>
 * Việc triển khai interface này giúp loại bỏ sự cần thiết của việc sử dụng `instanceof`
 * trong `PermissionEvaluator`, làm cho hệ thống tuân thủ nguyên tắc Mở/Đóng (Open/Closed Principle)
 * và dễ dàng mở rộng để hỗ trợ các loại tài nguyên mới.
 */
public interface Permissionable extends Serializable {
    /**
     * Lấy ID của tài nguyên. Giá trị này sẽ được sử dụng làm `targetId`
     * trong phương thức `hasPermission`.
     *
     * @return ID của tài nguyên, dưới dạng {@link Serializable}.
     */
    Serializable getPermissionResourceId();

    /**
     * Lấy loại của tài nguyên dưới dạng chuỗi. Giá trị này sẽ được sử dụng làm `targetType`
     * trong phương thức `hasPermission`.
     * <p>
     * Giá trị trả về nên tương ứng với một tên trong enum {@link vn.look.core.enums.Resource}.
     *
     * @return Chuỗi định danh loại tài nguyên (ví dụ: "ENTERPRISE").
     */
    String getPermissionResourceType();
}
