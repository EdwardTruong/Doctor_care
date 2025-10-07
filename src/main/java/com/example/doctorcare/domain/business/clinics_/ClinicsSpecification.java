package com.example.doctorcare.domain.business.clinics_;

import org.springframework.data.jpa.domain.Specification;

/**
 * Interface cho JPA Specifications của Clinics entity.
 * Cung cấp các methods để build dynamic queries một cách type-safe.
 */
public interface ClinicsSpecification {

    /**
     * Specification để filter clinics chưa bị soft delete
     */
    Specification<Clinics> notDeleted();

    /**
     * Specification để filter theo keyword (search trong name, address, description)
     */
    Specification<Clinics> withKeyword(String keyword);

    /**
     * Specification để filter theo place ID
     */
    Specification<Clinics> withPlaceId(Long placeId);

    /**
     * Specification để filter theo owner ID
     */
    Specification<Clinics> withOwnerId(Long ownerId);

    /**
     * Specification để filter theo tên clinic
     */
    Specification<Clinics> withName(String name);

    /**
     * Specification để filter theo phone number
     */
    Specification<Clinics> withPhone(String phone);

    /**
     * Specification để filter clinics có nhiều views (popular clinics)
     */
    Specification<Clinics> withMinViews(Integer minViews);

    /**
     * Specification để filter clinics theo địa chỉ
     */
    Specification<Clinics> withAddress(String address);

    /**
     * Specification để exclude một clinic ID cụ thể
     */
    Specification<Clinics> excludeId(Long excludeId);

    /**
     * Specification để filter clinics có doctors (có bác sĩ làm việc)
     */
    Specification<Clinics> withDoctors();

    /**
     * Specification tổng hợp cho search với tất cả filters
     */
    Specification<Clinics> withAllFilters(
            String keyword,
            Long placeId,
            Long ownerId);

    /**
     * Specification cho popular clinics (top clinics)
     */
    Specification<Clinics> popularClinics(Integer minViews);

    /**
     * Specification để kiểm tra clinic name uniqueness
     */
    Specification<Clinics> nameExistsExcludingId(String name, Long excludeId);

    /**
     * Specification cho search nâng cao với nhiều tiêu chí
     */
    Specification<Clinics> advancedSearch(
            String keyword,
            Long placeId,
            Long ownerId,
            Integer minViews,
            String address,
            Boolean withDoctorsOnly);
}
