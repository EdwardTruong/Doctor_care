package com.example.doctorcare.core.domain;

/**
 * Một interface hợp đồng (contract interface) cho các đối tượng domain có một "chủ sở hữu" rõ ràng.
 * <p>
 * Bất kỳ thực thể nào triển khai interface này đều cam kết cung cấp một phương thức để lấy
 * ID của chủ sở hữu. Điều này cho phép các thành phần hệ thống, đặc biệt là tầng bảo mật,
 * xử lý việc kiểm tra quyền sở hữu một cách đồng nhất cho nhiều loại tài nguyên khác nhau
 * mà không cần biết chi tiết về loại thực thể cụ thể.
 * <p>
 * Ví dụ, {@link vn.look.infrastructure.security.strategies.AbstractOwnableResourcePermissionStrategy}
 * sử dụng interface này để cung cấp một logic kiểm tra quyền sở hữu (isOwner) chung, có thể
 * tái sử dụng cho tất cả các tài nguyên "con" như Thành tựu (Achievement), Chứng chỉ (Certificate), v.v.
 *
 * @see vn.look.infrastructure.security.strategies.AbstractOwnableResourcePermissionStrategy
 * @param <T> Kiểu dữ liệu của định danh chủ sở hữu (thường là {@link Long}).
 */
public interface Ownable<T> {
    /**
     * Lấy ID định danh của chủ sở hữu tài nguyên này.
     * <p>
     * ID này thường là ID của một {@link vn.look.domain.system.user.User}.
     *
     * @return ID của chủ sở hữu. Trả về {@code null} nếu không xác định được chủ sở hữu.
     */
    T getOwnerId();
}