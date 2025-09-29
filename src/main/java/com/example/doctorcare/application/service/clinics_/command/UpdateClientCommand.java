package com.example.doctorcare.application.service.clinics.command;

import com.example.doctorcare.application.service.clinics.dto.ClinicsDto;
import com.example.doctorcare.core.cqrs.CommandWithResult;

/**
 * Cập nhật thông tin cần thiết của phòng khám.
 * 
 * @param name cập nhật lại cái tên
 * @param placeId  nếu vùng ở phòng khám đó xác nhập abc các kiểu.
 * @param phone  số đt.
 * @param address địa chỉ.
 * @param introductionHTML  giới thiệu về cái trang web phòng khám.
 * @param introductionMarkdown  giới thiệu.
 * @param description  mô tả
 */
public record UpdateClientCommand(
        Long id,
        String name,
        String phone,
        String address,
        String introductionHTML,
        String introductionMarkdown,
        String description,
        Long placeId

) implements CommandWithResult<ClinicsDto> {

}
