package com.example.doctorcare.domain.business.specializations;

import java.util.Optional;

import com.example.doctorcare.core.domain.BaseRepository;

public interface SpecializationsRepository extends BaseRepository<Specializations, Long>{
    
    /**
     * Find specialization by ID that is not soft deleted
     */
    default Optional<Specializations> findByIdAndDeletedFalse(Long id) {
        return findById(id).filter(spec -> spec.getDeleteAt() == null);
    }
}
