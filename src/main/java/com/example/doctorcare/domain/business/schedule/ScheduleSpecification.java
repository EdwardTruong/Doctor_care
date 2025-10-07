package com.example.doctorcare.domain.business.schedule;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;
import com.example.doctorcare.domain.business.doctor.Doctor;
import com.example.doctorcare.domain.business.specializations.Specializations;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

/**
 * JPA Specifications cho Schedule entity để dễ dàng build dynamic queries
 */
public class ScheduleSpecification {

    /**
     * Specification để filter schedules chưa bị soft delete
     */
    public static Specification<Schedule> notDeleted() {
        return (root, query, cb) -> cb.isNull(root.get("deleteAt"));
    }

    /**
     * Specification để filter theo keyword (search trong doctor name, time, specialization)
     */
    public static Specification<Schedule> withKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();
            }
            
            String likePattern = "%" + keyword.toLowerCase() + "%";
            
            // Join với doctor và specialization
            Join<Schedule, Doctor> doctorJoin = root.join("doctorEntity", JoinType.LEFT);
            Join<Schedule, Specializations> specializationJoin = root.join("specialization", JoinType.LEFT);
            
            return cb.or(
                cb.like(cb.lower(doctorJoin.get("fullName")), likePattern),
                cb.like(cb.lower(root.get("time")), likePattern),
                cb.like(cb.lower(specializationJoin.get("name")), likePattern)
            );
        };
    }

    /**
     * Specification để filter theo doctor ID
     */
    public static Specification<Schedule> withDoctorId(Long doctorId) {
        return (root, query, cb) -> {
            if (doctorId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("doctorEntity").get("id"), doctorId);
        };
    }

    /**
     * Specification để filter theo specialization ID
     */
    public static Specification<Schedule> withSpecializationId(Long specializationId) {
        return (root, query, cb) -> {
            if (specializationId == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("specialization").get("id"), specializationId);
        };
    }

    /**
     * Specification để filter theo date range (từ dateFrom đến dateTo)
     */
    public static Specification<Schedule> withDateRange(LocalDate dateFrom, LocalDate dateTo) {
        return (root, query, cb) -> {
            if (dateFrom == null && dateTo == null) {
                return cb.conjunction();
            }
            
            if (dateFrom != null && dateTo != null) {
                return cb.between(root.get("date"), dateFrom, dateTo);
            } else if (dateFrom != null) {
                return cb.greaterThanOrEqualTo(root.get("date"), dateFrom);
            } else {
                return cb.lessThanOrEqualTo(root.get("date"), dateTo);
            }
        };
    }

    /**
     * Specification để filter theo price range
     */
    public static Specification<Schedule> withPriceRange(Integer minPrice, Integer maxPrice) {
        return (root, query, cb) -> {
            if (minPrice == null && maxPrice == null) {
                return cb.conjunction();
            }
            
            if (minPrice != null && maxPrice != null) {
                return cb.between(root.get("price"), minPrice, maxPrice);
            } else if (minPrice != null) {
                return cb.greaterThanOrEqualTo(root.get("price"), minPrice);
            } else {
                return cb.lessThanOrEqualTo(root.get("price"), maxPrice);
            }
        };
    }

    /**
     * Specification để filter schedules có slot trống (available for booking)
     */
    public static Specification<Schedule> withAvailability(Boolean isAvailable) {
        return (root, query, cb) -> {
            if (isAvailable == null) {
                return cb.conjunction();
            }
            
            if (isAvailable) {
                // Available: maxBooking > sumBooking
                return cb.greaterThan(
                    cb.function("CAST", Integer.class, root.get("maxBooking"), cb.literal("integer")),
                    root.get("sumBooking")
                );
            } else {
                // Not available: maxBooking <= sumBooking
                return cb.lessThanOrEqualTo(
                    cb.function("CAST", Integer.class, root.get("maxBooking"), cb.literal("integer")),
                    root.get("sumBooking")
                );
            }
        };
    }

    /**
     * Specification để filter theo ngày (chỉ ngày cụ thể)
     */
    public static Specification<Schedule> withDate(LocalDate date) {
        return (root, query, cb) -> {
            if (date == null) {
                return cb.conjunction();
            }
            return cb.equal(root.get("date"), date);
        };
    }

    /**
     * Specification để filter schedules trong tương lai (từ hôm nay trở đi)
     */
    public static Specification<Schedule> fromToday() {
        return (root, query, cb) -> 
            cb.greaterThanOrEqualTo(root.get("date"), LocalDate.now());
    }

    /**
     * Specification để filter theo time slot
     */
    public static Specification<Schedule> withTimeSlot(String time) {
        return (root, query, cb) -> {
            if (time == null || time.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("time"), time);
        };
    }

    /**
     * Specification để exclude một schedule ID cụ thể (dùng cho update conflict check)
     */
    public static Specification<Schedule> excludeId(Long excludeId) {
        return (root, query, cb) -> {
            if (excludeId == null) {
                return cb.conjunction();
            }
            return cb.notEqual(root.get("id"), excludeId);
        };
    }

    /**
     * Specification để kiểm tra conflict scheduling
     */
    public static Specification<Schedule> hasConflict(Long doctorId, LocalDate date, String time, Long excludeId) {
        return Specification.where(notDeleted())
            .and(withDoctorId(doctorId))
            .and(withDate(date))
            .and(withTimeSlot(time))
            .and(excludeId(excludeId));
    }

    /**
     * Specification tổng hợp cho search với tất cả filters
     */
    public static Specification<Schedule> withAllFilters(
            String keyword,
            LocalDate dateFrom,
            LocalDate dateTo,
            Integer minPrice,
            Integer maxPrice,
            Long doctorId,
            Long specializationId,
            Boolean isAvailable) {
        
        return Specification.where(notDeleted())
            .and(withKeyword(keyword))
            .and(withDateRange(dateFrom, dateTo))
            .and(withPriceRange(minPrice, maxPrice))
            .and(withDoctorId(doctorId))
            .and(withSpecializationId(specializationId))
            .and(withAvailability(isAvailable));
    }
}