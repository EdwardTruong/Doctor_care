package com.example.doctorcare.application.service.place_.command;

import com.example.doctorcare.core.cqrs.Command;

/**'
 * Lệnh để xóa một vùng
 * @param id Id của vùng muốn xóa
 */
public record DeletePlaceCommand(Long id) implements Command {
    
}
