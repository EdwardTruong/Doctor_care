package com.example.doctorcare.domain.business.clinics_;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import com.example.doctorcare.domain.business.places.Places;
import com.example.doctorcare.domain.system.user.User;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;

/**
 * Implementation của ClinicsSpecification interface.
 * Cung cấp các methods để build JPA Specifications cho Clinics entity.
 */
@Component
public class ClinicsSpecificationImpl implements ClinicsSpecification {

    @Override
    public Specification<Clinics> notDeleted() {
        return (root, query, cb) -> cb.equal(root.get("deleted"), false);
    }

    @Override
    public Specification<Clinics> withKeyword(String keyword) {
        return (root, query, cb) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return cb.conjunction();
            }
            
            String likePattern = "%" + keyword.toLowerCase() + "%";
            
            return cb.or(
                cb.like(cb.lower(root.get("name")), likePattern),
                cb.like(cb.lower(root.get("address")), likePattern),
                cb.like(cb.lower(root.get("description")), likePattern)
            );
        };
    }

    @Override
    public Specification<Clinics> withPlaceId(Long placeId) {
        return (root, query, cb) -> {
            if (placeId == null) {
                return cb.conjunction();
            }
            
            Join<Clinics, Places> placeJoin = root.join("place", JoinType.LEFT);
            return cb.equal(placeJoin.get("id"), placeId);
        };
    }

    @Override
    public Specification<Clinics> withOwnerId(Long ownerId) {
        return (root, query, cb) -> {
            if (ownerId == null) {
                return cb.conjunction();
            }
            
            Join<Clinics, User> ownerJoin = root.join("owner", JoinType.LEFT);
            return cb.equal(ownerJoin.get("id"), ownerId);
        };
    }

    @Override
    public Specification<Clinics> withName(String name) {
        return (root, query, cb) -> {
            if (name == null || name.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(cb.lower(root.get("name")), name.toLowerCase());
        };
    }

    @Override
    public Specification<Clinics> withPhone(String phone) {
        return (root, query, cb) -> {
            if (phone == null || phone.trim().isEmpty()) {
                return cb.conjunction();
            }
            return cb.equal(root.get("phone"), phone);
        };
    }

    @Override
    public Specification<Clinics> withMinViews(Integer minViews) {
        return (root, query, cb) -> {
            if (minViews == null) {
                return cb.conjunction();
            }
            return cb.greaterThanOrEqualTo(root.get("view"), minViews);
        };
    }

    @Override
    public Specification<Clinics> withAddress(String address) {
        return (root, query, cb) -> {
            if (address == null || address.trim().isEmpty()) {
                return cb.conjunction();
            }
            String likePattern = "%" + address.toLowerCase() + "%";
            return cb.like(cb.lower(root.get("address")), likePattern);
        };
    }

    @Override
    public Specification<Clinics> excludeId(Long excludeId) {
        return (root, query, cb) -> {
            if (excludeId == null) {
                return cb.conjunction();
            }
            return cb.notEqual(root.get("id"), excludeId);
        };
    }

    @Override
    public Specification<Clinics> withDoctors() {
        return (root, query, cb) -> {
            // Kiểm tra size của listDoctor > 0
            return cb.greaterThan(cb.size(root.get("listDoctor")), 0);
        };
    }

    @Override
    public Specification<Clinics> withAllFilters(
            String keyword,
            Long placeId,
            Long ownerId) {
        
        return Specification.where(notDeleted())
            .and(withKeyword(keyword))
            .and(withPlaceId(placeId))
            .and(withOwnerId(ownerId));
    }

    @Override
    public Specification<Clinics> popularClinics(Integer minViews) {
        return Specification.where(notDeleted())
            .and(withMinViews(minViews))
            .and(withDoctors()); // Only clinics with doctors
    }

    @Override
    public Specification<Clinics> nameExistsExcludingId(String name, Long excludeId) {
        return Specification.where(notDeleted())
            .and(withName(name))
            .and(excludeId(excludeId));
    }

    @Override
    public Specification<Clinics> advancedSearch(
            String keyword,
            Long placeId,
            Long ownerId,
            Integer minViews,
            String address,
            Boolean withDoctorsOnly) {
        
        Specification<Clinics> spec = Specification.where(notDeleted());
        
        if (keyword != null) {
            spec = spec.and(withKeyword(keyword));
        }
        
        if (placeId != null) {
            spec = spec.and(withPlaceId(placeId));
        }
        
        if (ownerId != null) {
            spec = spec.and(withOwnerId(ownerId));
        }
        
        if (minViews != null) {
            spec = spec.and(withMinViews(minViews));
        }
        
        if (address != null) {
            spec = spec.and(withAddress(address));
        }
        
        if (withDoctorsOnly != null && withDoctorsOnly) {
            spec = spec.and(withDoctors());
        }
        
        return spec;
    }
}