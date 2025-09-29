package com.example.doctorcare.application.service.place_.command;

import com.example.doctorcare.application.service.place_.dto.PlaceDto;
import com.example.doctorcare.core.cqrs.CommandWithResult;

/**
 * Tạo mới 1 vùng nếu vùng đó chưa có dưới hệ thống
 * @param name tên của vùng
 */
public record CreatePlaceCommand(
    String name
    // ...... xóa bớt đi không cần thiết.
) implements CommandWithResult<PlaceDto> {
    
}
