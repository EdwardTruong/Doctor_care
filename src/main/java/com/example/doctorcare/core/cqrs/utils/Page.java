package com.example.doctorcare.core.cqrs.utils;

import java.util.List;

/**
 * Một interface trừu tượng cho kết quả phân trang.
 * <p>
 * Interface này định nghĩa một cấu trúc chung cho dữ liệu phân trang, giúp tách biệt
 * lớp ứng dụng (application layer) khỏi các chi tiết triển khai cụ thể của tầng
 * truy cập dữ liệu (ví dụ: {@code org.springframework.data.domain.Page}).
 * Việc sử dụng interface này đảm bảo tính nhất quán cho các API trả về dữ liệu
 * phân trang trong toàn bộ hệ thống.
 *
 * @param <T> Kiểu dữ liệu của các phần tử trong trang.
 */
public interface Page<T> {

	/**
	 * Trả về tổng số trang.
	 * @return tổng số trang.
	 */
	long getTotalPages();

	/**
	 * Trả về tổng số phần tử trên tất cả các trang.
	 * @return tổng số phần tử.
	 */
	long getTotalElements();

	/**
	 * Trả về kích thước của trang (số phần tử tối đa trên một trang).
	 * @return kích thước trang.
	 */
	long getPageSize();

	/**
	 * Trả về số thứ tự của trang hiện tại (bắt đầu từ 0).
	* @return số thứ tự trang hiện tại.
	 */
	long getPageNumber();

	/**
	 * Trả về danh sách các phần tử của trang hiện tại.
	 * @return một {@link List} chứa các phần tử của trang.
	 */
	List<T> getItems();

}
