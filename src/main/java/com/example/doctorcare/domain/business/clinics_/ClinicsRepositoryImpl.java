package com.example.doctorcare.domain.business.clinics_;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

/**
 * Implementation của ClinicsRepositoryCustom sử dụng JPA Criteria API
 * và ClinicsSpecification để build dynamic queries một cách type-safe.
 */
@Repository
public class ClinicsRepositoryImpl implements ClinicsRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    
    @Autowired
    private ClinicsSpecification clinicsSpecification;

    @Override
    public Page<Clinics> searchWithFilters(String keyword, Long placeId, Long ownerId, Pageable pageable) {
        Specification<Clinics> spec = clinicsSpecification.withAllFilters(keyword, placeId, ownerId);
        return findWithSpecification(spec, pageable);
    }

    @Override
    public Page<Clinics> findPopularClinics(Integer minViews, Pageable pageable) {
        Specification<Clinics> spec = clinicsSpecification.popularClinics(minViews);
        
        // Custom sort cho popular clinics: view desc, số lượng doctors desc
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Clinics> query = cb.createQuery(Clinics.class);
        Root<Clinics> root = query.from(Clinics.class);
        
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Clinics> countRoot = countQuery.from(Clinics.class);
        
        // Áp dụng specification
        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, query, cb);
            if (predicate != null) {
                query.where(predicate);
            }
            
            Predicate countPredicate = spec.toPredicate(countRoot, countQuery, cb);
            if (countPredicate != null) {
                countQuery.select(cb.count(countRoot)).where(countPredicate);
            }
        }
        
        // Custom sort cho popular clinics
        query.orderBy(
            cb.desc(root.get("view")),
            cb.desc(cb.size(root.get("listDoctor")))
        );
        
        // Thực thi queries
        Long total = entityManager.createQuery(countQuery).getSingleResult();
        List<Clinics> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
                
        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public Page<Clinics> findWithSpecification(Specification<Clinics> specification, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Query để lấy dữ liệu
        CriteriaQuery<Clinics> query = cb.createQuery(Clinics.class);
        Root<Clinics> root = query.from(Clinics.class);

        // Query để đếm tổng số bản ghi
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Clinics> countRoot = countQuery.from(Clinics.class);

        // Áp dụng specification
        if (specification != null) {
            Predicate predicate = specification.toPredicate(root, query, cb);
            if (predicate != null) {
                query.where(predicate);
            }

            Predicate countPredicate = specification.toPredicate(countRoot, countQuery, cb);
            if (countPredicate != null) {
                countQuery.select(cb.count(countRoot)).where(countPredicate);
            } else {
                countQuery.select(cb.count(countRoot));
            }
        }

        // Áp dụng sắp xếp
        query.orderBy(org.springframework.data.jpa.repository.query.QueryUtils.toOrders(pageable.getSort(), root, cb));

        // Thực thi queries
        Long total = entityManager.createQuery(countQuery).getSingleResult();
        List<Clinics> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(resultList, pageable, total);
    }

    @Override
    public long countWithFilters(String keyword, Long placeId, Long ownerId) {
        Specification<Clinics> spec = clinicsSpecification.withAllFilters(keyword, placeId, ownerId);
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Clinics> root = countQuery.from(Clinics.class);
        
        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, countQuery, cb);
            if (predicate != null) {
                countQuery.select(cb.count(root)).where(predicate);
            } else {
                countQuery.select(cb.count(root));
            }
        }
        
        return entityManager.createQuery(countQuery).getSingleResult();
    }

    @Override
    public boolean existsByNameExcludingId(String name, Long excludeId) {
        Specification<Clinics> spec = clinicsSpecification.nameExistsExcludingId(name, excludeId);
        
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = cb.createQuery(Long.class);
        Root<Clinics> root = query.from(Clinics.class);
        
        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, query, cb);
            if (predicate != null) {
                query.select(cb.count(root)).where(predicate);
            } else {
                query.select(cb.count(root));
            }
        }
        
        Long count = entityManager.createQuery(query).getSingleResult();
        return count > 0;
    }

    @Override
    public Page<Clinics> findClinicsWithDoctors(Pageable pageable) {
        Specification<Clinics> spec = Specification.where(clinicsSpecification.notDeleted())
                .and(clinicsSpecification.withDoctors());
                
        // Custom sort cho clinics with doctors: sắp xếp theo số lượng doctors desc
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Clinics> query = cb.createQuery(Clinics.class);
        Root<Clinics> root = query.from(Clinics.class);
        
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Clinics> countRoot = countQuery.from(Clinics.class);
        
        // Áp dụng specification
        if (spec != null) {
            Predicate predicate = spec.toPredicate(root, query, cb);
            if (predicate != null) {
                query.where(predicate);
            }
            
            Predicate countPredicate = spec.toPredicate(countRoot, countQuery, cb);
            if (countPredicate != null) {
                countQuery.select(cb.count(countRoot)).where(countPredicate);
            }
        }
        
        // Custom sort: số lượng doctors desc
        query.orderBy(cb.desc(cb.size(root.get("listDoctor"))));
        
        // Thực thi queries
        Long total = entityManager.createQuery(countQuery).getSingleResult();
        List<Clinics> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
                
        return new PageImpl<>(resultList, pageable, total);
    }
}