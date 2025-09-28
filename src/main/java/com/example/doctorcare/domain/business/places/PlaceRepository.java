package com.example.doctorcare.domain.business.places;

import java.util.Optional;

import com.example.doctorcare.core.domain.BaseRepository;

public interface PlaceRepository extends BaseRepository<Places, Long>{
   Optional<Places> findByNameAndDeleted(String name, boolean status);
}
