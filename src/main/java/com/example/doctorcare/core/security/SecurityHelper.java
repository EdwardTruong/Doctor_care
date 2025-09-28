package com.example.doctorcare.core.security;

import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.doctorcare.domain.system.user.User;


/**
 * Utility class for Spring Security.
 */
public final class SecurityHelper {

    private SecurityHelper() {
    }

    /**
     * Lấy người dùng hiện tại từ Security Context. Đây là cách được khuyến nghị để lấy người dùng hiện tại.
     *
     * @return Một Optional chứa thực thể User, hoặc Optional rỗng nếu không tìm thấy hoặc chưa xác thực.
     */
    public static Optional<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return Optional.empty();
        }
        Object principal = authentication.getPrincipal();
        
        // Giả định sử dụng một implementation UserDetails tùy chỉnh chứa thực thể User.
        // Đây là một thực hành phổ biến và được khuyến nghị.
        if (principal instanceof CustomUserDetails customUserDetails) {
            return Optional.of(customUserDetails.getUser());
        }
        if (principal instanceof User user) { // Fallback for tests or other configurations
            return Optional.of(user);
        }
        return Optional.empty();
    }

    /**
     * Lấy người dùng hiện tại từ đối tượng Authentication.
     * @deprecated Sử dụng {@link #getCurrentUser()} để thay thế để có sự nhất quán và an toàn hơn.
     */
    @Deprecated(since = "1.1.0", forRemoval = true)
    public static User getCurrentUser(Authentication authentication) {
        return getCurrentUser().orElse(null);
    }
}