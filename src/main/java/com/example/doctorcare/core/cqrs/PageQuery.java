package com.example.doctorcare.core.cqrs;

import org.springframework.data.domain.Pageable;

import com.example.doctorcare.core.cqrs.utils.Page;


/**
 * Một interface đại diện cho một Query trả về kết quả dạng phân trang (Page) trong mẫu CQRS.
 * <p>
 * Bất kỳ Query nào implement interface này đều phải cung cấp một đối tượng {@link Pageable}.
 *
 * @param <R> Kiểu dữ liệu của các phần tử trong trang kết quả.
 */
public interface PageQuery<R> extends Query<Page<R>> {

    /** Trả về thông tin phân trang và sắp xếp. */
    Pageable pageable();
}