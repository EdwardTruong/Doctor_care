package com.example.doctorcare.domain.business.clinics_;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

/**
 * Interface cho các phương thức truy vấn clinic tùy chỉnh sử dụng JPA Specifications.
 * Cung cấp các method để build dynamic queries một cách type-safe và maintainable.
 */
public interface ClinicsRepositoryCustom {

    /**
     * Tìm kiếm clinics với các điều kiện động sử dụng Specifications
     *
     * @param keyword Từ khóa tìm kiếm trong tên, địa chỉ, mô tả
     * @param placeId ID của place để filter
     * @param ownerId ID của owner để filter
     * @param pageable Thông tin phân trang
     * @return Một trang các clinic thỏa mãn điều kiện
     */
    Page<Clinics> searchWithFilters(String keyword, Long placeId, Long ownerId, Pageable pageable);

    /**
     * Tìm top clinics phổ biến nhất
     *
     * @param minViews Số views tối thiểu
     * @param pageable Thông tin phân trang
     * @return Danh sách top clinics
     */
    Page<Clinics> findPopularClinics(Integer minViews, Pageable pageable);

    /**
     * Tìm clinics theo specification tùy chỉnh
     *
     * @param specification JPA Specification
     * @param pageable Thông tin phân trang
     * @return Kết quả tìm kiếm
     */
    Page<Clinics> findWithSpecification(Specification<Clinics> specification, Pageable pageable);

    /**
     * Đếm số clinics thỏa mãn điều kiện
     *
     * @param keyword Từ khóa tìm kiếm
     * @param placeId ID của place
     * @param ownerId ID của owner
     * @return Số lượng clinics
     */
    long countWithFilters(String keyword, Long placeId, Long ownerId);

    /**
     * Kiểm tra clinic name có tồn tại không (trừ clinic hiện tại)
     *
     * @param name Tên clinic
     * @param excludeId ID clinic cần loại trừ
     * @return true nếu tên đã tồn tại
     */
    boolean existsByNameExcludingId(String name, Long excludeId);

    /**
     * Tìm clinics có ít nhất một doctor
     *
     * @param pageable Thông tin phân trang
     * @return Danh sách clinics có doctors
     */
    Page<Clinics> findClinicsWithDoctors(Pageable pageable);
}