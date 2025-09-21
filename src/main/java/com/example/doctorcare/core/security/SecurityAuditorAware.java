package com.example.doctorcare.core.security;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import com.example.doctorcare.core.domain.BaseConstants;

import com.example.doctorcare.domain.system.user.User;

/**
 * Triển khai {@link AuditorAware} để cung cấp thông tin người dùng hiện tại
 * cho cơ chế JPA Auditing.
 * <p>
 * Lớp này tích hợp với Spring Security để tự động điền các trường
 * {@code @CreatedBy} và {@code @LastModifiedBy} trong các entity.
 * Nó sử dụng {@link com.example.doctorcare.core.security.PermissionHelper} để lấy thông tin người dùng một cách nhất quán
 * và an toàn.
 */
@Component
public class SecurityAuditorAware implements AuditorAware<String> {

    /**
     * Lấy tên đăng nhập (username) của người dùng hiện tại từ Security Context.
     * <p>
     * Nếu người dùng đã được xác thực, phương thức sẽ trả về username của họ.
     * Nếu không, nó sẽ trả về một giá trị mặc định là "anonymous" từ {@link BaseConstants}.
     *
     * @return một {@link Optional} chứa username của auditor hiện tại.
     */
    @Override
    public Optional<String> getCurrentAuditor() {
        // Sử dụng SecurityHelper để lấy người dùng hiện tại một cách nhất quán.
        // Đây là cách làm được khuyến nghị trong dự án.
        return SecurityHelper.getCurrentUser()
                .map(User::getUsername) // Lấy username từ đối tượng User
                .or(() -> Optional.of(BaseConstants.ANONYMOUS)); // Nếu không có user, trả về ANONYMOUS
    }
}
