package com.example.doctorcare.application.service.place_.querry;

import com.example.doctorcare.application.service.place_.dto.PlaceDto;
import com.example.doctorcare.core.cqrs.Query;

/**
 * Query dùng để lấy chi tiết thông qua ID
 * @param placeId Id vùng
 */
public record GetPlaceDetailQuery(Long placeId) implements Query<PlaceDto> {
    
}
