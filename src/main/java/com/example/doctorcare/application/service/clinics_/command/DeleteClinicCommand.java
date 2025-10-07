package com.example.doctorcare.application.service.clinics_.command;

import com.example.doctorcare.core.cqrs.Command;

/**'
 * Lệnh để xóa 
 * @param id Id thuộc entity muốn xóa
 */
public record DeleteClinicCommand(Long id) implements Command {
    
}
