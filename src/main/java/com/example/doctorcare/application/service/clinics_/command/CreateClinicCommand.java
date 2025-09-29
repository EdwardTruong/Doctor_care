package com.example.doctorcare.application.service.clinics.command;

import com.example.doctorcare.application.service.clinics.dto.ClinicsDto;
import com.example.doctorcare.core.cqrs.CommandWithResult;

/**
 * Tạo mới 1 vùng nếu vùng đó chưa có dưới hệ thống
 * @param name tên của phòng khám 
 * @param phone tên của phòng khám 
 * @param address tên của phòng khám 
 * @param introductionHTML tên của phòng khám 
 * @param introductionMarkdown tên của phòng khám 
 * @param description tên của phòng khám 
 * @param introductionMarkdown tên của phòng khám 
 */
public record CreateClinicCommand(

                String name,
                String phone,
                String address,
                String introductionHTML,
                String introductionMarkdown,
                String description,
                Long placeId,
                Long owerId // UserId 
) implements CommandWithResult<ClinicsDto> {}
