package com.example.doctorcare.application.service.place.command;

import org.springframework.data.domain.Pageable;

import com.example.doctorcare.application.service.place.dto.PlaceDto;
import com.example.doctorcare.core.cqrs.PageQuery;

public record GetPlaceQuery(
        Long id,
        String keyword,
        Pageable pageable) implements PageQuery<PlaceDto> {

}
