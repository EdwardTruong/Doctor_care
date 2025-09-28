package com.example.doctorcare.application.service.place.command;

import com.example.doctorcare.application.service.place.dto.PlaceDto;
import com.example.doctorcare.core.cqrs.CommandWithResult;

/**
 * Cập nhật thông tin cần thiết của vùng.
 * @param name cập nhật lại cái tên
 * @param ids danh sách cha tổ tiên nhà nó.  
 */
public record UpdatePlaceCommand(
    Long placeId,
    String name,
    Long parentId 
) implements CommandWithResult<PlaceDto>{
    
}
