package com.example.doctorcare.infrastructure.security.strategies;


import org.springframework.data.jpa.repository.JpaRepository;

import com.example.doctorcare.core.domain.Ownable;

import lombok.RequiredArgsConstructor;

/**
 * Một lớp trừu tượng chuyên biệt cho các tài nguyên triển khai interface {@link Ownable}.
 * Lớp này cung cấp một cách triển khai chung cho phương thức {@code isOwner},
 * giúp đơn giản hóa việc tạo các strategy cho các tài nguyên con (ví dụ: Achievement, Certificate).
 * <p>
 * <b>Lưu ý về hiệu năng:</b> Việc triển khai {@code isOwner} trong lớp này sẽ
 * thực hiện một truy vấn đến cơ sở dữ liệu để tải thực thể. Đây là một sự đánh đổi
 * có chủ đích để đơn giản hóa code, và nó được chấp nhận cho các tài nguyên con,
 * nơi việc kiểm tra quyền sở hữu thường đi kèm với việc đã tải thực thể đó ở tầng service.
 * Nó không nên được sử dụng cho các tài nguyên cấp cao như Enterprise hay Entrepreneur,
 * nơi việc kiểm tra quyền sở hữu đã được tối ưu hóa qua UserContext.
 * 
 * @param <T> Loại thực thể, phải triển khai Ownable với ID chủ sở hữu là Long.
 */
@RequiredArgsConstructor
public abstract class AbstractOwnableResourcePermissionStrategy<T extends Ownable<Long>> extends AbstractResourcePermissionStrategy {

    private final JpaRepository<T, Long> repository;

    /**
     * {@inheritDoc}
     * <p>
     * Triển khai này kiểm tra quyền sở hữu bằng cách:
     * <ol>
     *     <li>Tải thực thể từ cơ sở dữ liệu bằng {@code targetId}.</li>
     *     <li>Gọi phương thức {@code getOwnerId()} từ interface {@link Ownable}.</li>
     *     <li>So sánh ID chủ sở hữu với ID của người dùng trong {@code userContext}.</li>
     * </ol>
     * <b>Cảnh báo:</b> Thao tác này có truy vấn CSDL.
     */
    @Override
    public boolean isOwner(UserContext userContext, Long targetId) {
        return repository.findById(targetId)
                .map(ownableEntity -> userContext.getUser().id().equals(ownableEntity.getOwnerId()))
                .orElse(false);
    }
}
