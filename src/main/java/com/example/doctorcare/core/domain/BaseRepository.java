package com.example.doctorcare.core.domain;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;


/**
 * Interface repository cơ sở cho các thực thể kế thừa từ {@link BaseEntity}.
 * <p>
 * Cung cấp các phương thức CRUD cơ bản và các phương thức truy vấn chung có tính đến
 * cờ xóa mềm (soft-delete). Việc sử dụng {@link NoRepositoryBean} ngăn Spring Data
 * tạo một bean triển khai cho interface này, vì nó chỉ dùng để kế thừa.
 *
 * @param <T> Kiểu thực thể, phải kế thừa từ {@link BaseEntity}.
 * @param <I> Kiểu của định danh (ID) của thực thể.
 */
@NoRepositoryBean
public interface BaseRepository<T extends BaseEntity<I>, I>
		extends JpaRepository<T, I>, JpaSpecificationExecutor<T>, QuerydslPredicateExecutor<T> {
	
    /**
     * Tìm một thực thể theo ID, chỉ trả về kết quả nếu thực thể đó chưa bị xóa mềm.
     *
     * @param id ID của thực thể cần tìm.
     * @return một {@link Optional} chứa thực thể nếu tìm thấy và chưa bị xóa, ngược lại là rỗng.
     */
	Optional<T> findByIdAndDeletedFalse(I id);

    /**
     * Lấy một trang các thực thể chưa bị xóa mềm.
     *
     * @param pageable thông tin phân trang.
     * @return một {@link Page} chứa các thực thể.
     */
	Page<T> findAllByDeletedFalse(Pageable pageable);

}