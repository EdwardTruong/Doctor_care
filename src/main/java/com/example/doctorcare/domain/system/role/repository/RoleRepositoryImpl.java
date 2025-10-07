package com.example.doctorcare.domain.system.role.repository;


import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.example.doctorcare.core.domain.BaseEntity_;
import com.example.doctorcare.domain.system.role.model.Role;
import com.example.doctorcare.domain.system.role.model.RoleType;
import com.example.doctorcare.domain.system.role.model.Role_;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@Repository
public class RoleRepositoryImpl implements RoleRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Role> search(String keyword, RoleType roleType, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // Query để lấy dữ liệu
        CriteriaQuery<Role> query = cb.createQuery(Role.class);
        Root<Role> root = query.from(Role.class);

        // Query để đếm tổng số bản ghi
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Role> countRoot = countQuery.from(Role.class);

        // Xây dựng các điều kiện
        List<Predicate> predicates = buildPredicates(cb, root, keyword, roleType);
        query.where(predicates.toArray(new Predicate[0]));

        List<Predicate> countPredicates = buildPredicates(cb, countRoot, keyword, roleType);
        countQuery.select(cb.count(countRoot)).where(countPredicates.toArray(new Predicate[0]));

        // Áp dụng sắp xếp
        query.orderBy(org.springframework.data.jpa.repository.query.QueryUtils.toOrders(pageable.getSort(), root, cb));

        Long total = entityManager.createQuery(countQuery).getSingleResult();
        List<Role> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(resultList, pageable, total);
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Role> root, String keyword, RoleType roleType) {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get(BaseEntity_.deleted), false));

        if (StringUtils.hasText(keyword)) {
            predicates.add(cb.like(cb.lower(root.get(Role_.roleName)), "%" + keyword.toLowerCase() + "%"));
        }
        if (roleType != null) {
            predicates.add(cb.equal(root.get(Role_.roleType), roleType));
        }
        return predicates;
    }
}