package com.example.doctorcare.core.domain;


import java.time.Instant;

/**
 * Một interface hợp đồng (contract interface) cho các thực thể có khả năng kiểm toán (auditable).
 * <p>
 * Bất kỳ thực thể nào triển khai interface này đều cam kết cung cấp thông tin về việc
 * ai đã tạo và sửa đổi nó, và vào thời điểm nào. Điều này cho phép hệ thống xử lý
 * việc kiểm toán một cách đồng nhất.
 * <p>
 * Lớp {@link BaseEntity} là lớp triển khai chính của interface này.
 *
 * @see BaseEntity
 * @see com.example.doctorcare.core.security.SecurityAuditorAware
 */
public interface AuditableEntity {

    /**
     * Lấy tên người dùng đã tạo thực thể.
     */
    String getCreatedBy();

    /**
     * Lấy thời điểm thực thể được tạo.
     */
    Instant getCreatedAt();

    /**
     * Lấy tên người dùng đã sửa đổi thực thể lần cuối.
     */
    String getUpdatedBy();

    /**
     * Lấy thời điểm thực thể được sửa đổi lần cuối.
     */
    Instant getUpdatedAt();
}
