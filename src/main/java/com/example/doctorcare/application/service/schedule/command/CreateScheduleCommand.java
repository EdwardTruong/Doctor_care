package com.example.doctorcare.application.service.schedule.command;

import java.time.LocalDate;
import java.time.LocalTime;
import com.example.doctorcare.application.service.schedule.dto.ScheduleDto;
import com.example.doctorcare.core.cqrs.CommandWithResult;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Command để tạo mới Schedule
 */
public record CreateScheduleCommand(

        @NotNull(message = "Date is required") LocalDate date, // Ngày khám của lịch này

        @NotNull(message = "Time start is required") LocalTime startTime,

        @NotNull(message = "Time end is required") LocalTime endTime,

        @NotNull(message = "Max booking is required") String maxBooking, // tổng số booking dự tính trong khung giờ

        @Positive(message = "Price must be positive") Double price, // giá khám

        @PositiveOrZero(message = "Sum booking must be zero or positive") 
        Integer sumBooking, // Mỗi booking thành công thì ++ 

        @NotNull(message = "Doctor ID is required") Long doctorId, // id của doc

        Long specializationId // Chuyên ngày mà doc đã đăng ký để khám. Mặc định 1 lịch khám của doc
                              // chỉ có 1 chuyên ngành. Ví dụ (răng-hàm-mặt || tim mạch ..ect)

) implements CommandWithResult<ScheduleDto> {

    /**
     * Constructor with default values
     */
    public CreateScheduleCommand {
        // Set default sumBooking to 0 if null
        if (sumBooking == null) {
            sumBooking = 0;
        }
    }

    /**
     * Validate time format (basic validation)
     * 
     * 
        // Basic time format validation (HH:mm or HH:mm-HH:mm) // Nếu FE trả về giá trị có ss (giây thì có thể gây ra lỗi.)
        // String timePattern = "^([0-1]?[0-9]|2[0-3]):[0-5][0-9](-([0-1]?[0-9]|2[0-3]):[0-5][0-9])?$";
        // return startTime.toString().matches(timePattern) && endTime.toString().matches(timePattern);
     */
    public boolean isValidTimeFormat() {
        return startTime != null && endTime != null && !endTime.isBefore(startTime);
    }

    /**
     * Validate maxBooking format (should be numeric)
     */
    public boolean isValidMaxBooking() {
        if (maxBooking == null || maxBooking.trim().isEmpty()) {
            return false;
        }
        try {
            int max = Integer.parseInt(maxBooking.trim());
            return max > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if sumBooking exceeds maxBooking
     */
    public boolean isValidBookingCount() {
        try {
            int max = Integer.parseInt(maxBooking.trim());
            return sumBooking <= max;
        } catch (NumberFormatException e) {
            return true; // Let other validation handle the maxBooking format
        }
    }
}
