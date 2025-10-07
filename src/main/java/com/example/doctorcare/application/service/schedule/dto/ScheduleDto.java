package com.example.doctorcare.application.service.schedule.dto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import com.example.doctorcare.domain.business.schedule.Schedule;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScheduleDto {

    private Long id;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate date;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime startTime;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
    private LocalTime endTime;
    
    private Integer maxBooking;
    private Double price;
    private Integer sumBooking;
    
    // Doctor information
    private Long doctorId;
    private String doctorName;
    private String doctorEmail;
    
    // Specialization information
    private Long specializationId;
    private String specializationName;
    
    // Booking availability
    private Integer availableBookings;
    private Boolean isFullyBooked;
    private Double bookingPercentage;
    
    // Audit fields
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Instant createdAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Instant updatedAt;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private Instant deletedAt;
    
    private Boolean isDeleted;
    
    /**
     * Convert từ Schedule entity sang ScheduleDto
     */
    public static ScheduleDto fromEntity(Schedule schedule) {

        if (schedule == null) return null;
        
        // Calculate derived fields
        Integer maxBookingNum = schedule.getMaxBooking();
        Integer currentBookings = schedule.getSumBooking() != null ? schedule.getSumBooking() : 0;
        Integer availableBookings = maxBookingNum != null ? Math.max(0, maxBookingNum - currentBookings) : null;
        Boolean isFullyBooked = maxBookingNum != null && currentBookings >= maxBookingNum;
        Double bookingPercentage = maxBookingNum != null && maxBookingNum > 0 ? 
            (currentBookings.doubleValue() / maxBookingNum.doubleValue()) * 100 : null;

        return ScheduleDto.builder()
                .id(schedule.getId())
                .date(schedule.getDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .maxBooking(schedule.getMaxBooking())
                .price(schedule.getPrice())
                .sumBooking(currentBookings)
                
                // Doctor info
                .doctorId(schedule.getDoctor() != null ? schedule.getDoctor().getId() : null)
                .doctorName(schedule.getDoctor() != null ? schedule.getDoctor().getUser().getFullName() : null)
                .doctorEmail(schedule.getDoctor() != null ? schedule.getDoctor().getUser().getAddressEmail() : null)
                
                // Specialization info
                .specializationId(schedule.getSpecialization() != null ? schedule.getSpecialization().getId() : null)
                .specializationName(schedule.getSpecialization() != null ? schedule.getSpecialization().getName() : null)
                
                // Calculated fields
                .availableBookings(availableBookings)
                .isFullyBooked(isFullyBooked)
                .bookingPercentage(bookingPercentage)
                
                // Audit fields
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }
    

    /**
     * Convert từ Schedule entity sang ScheduleDto (basic info only)
     */
    public static ScheduleDto fromEntityBasic(Schedule schedule) {
        if (schedule == null) return null;
        
        return ScheduleDto.builder()
                .id(schedule.getId())
                .date(schedule.getDate())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .maxBooking(schedule.getMaxBooking())
                .price(schedule.getPrice())
                .sumBooking(schedule.getSumBooking())
                .doctorId(schedule.getDoctor() != null ? schedule.getDoctor().getId() : null)
                .specializationId(schedule.getSpecialization() != null ? schedule.getSpecialization().getId() : null)
                .createdAt(schedule.getCreatedAt())
                .updatedAt(schedule.getUpdatedAt())
                .build();
    }
    
    /**
     * Parse maxBooking to ensure it's valid
     */
    private static Integer parseMaxBooking(Integer maxBooking) {
        if (maxBooking == null || maxBooking <= 0) {
            return null;
        }
        return maxBooking;
    }
    
    /**
     * Get formatted price display
     */
    public String getPriceDisplay() {
        return price != null ? String.format("%,.0f VND", price) : "Free";
    }
    
    /**
     * Get booking status text
     */
    public String getBookingStatusText() {
        if (isFullyBooked != null && isFullyBooked) {
            return "Fully Booked";
        }
        if (availableBookings != null && availableBookings > 0) {
            return availableBookings + " slots available";
        }
        return "No bookings available";
    }
    
    /**
     * Check if schedule is available for booking
     */
    public boolean isAvailableForBooking() {
        return availableBookings != null && availableBookings > 0 && 
               (isFullyBooked == null || !isFullyBooked) &&
               (isDeleted == null || !isDeleted);
    }
}