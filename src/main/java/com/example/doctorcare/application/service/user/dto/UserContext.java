package com.example.doctorcare.application.service.user.dto;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;


@Builder(toBuilder = true)
@Schema(description = "DTO chứa thông tin chi tiết về tất cả các vai trò và mối quan hệ của người dùng trong hệ thống.")
public record UserContext(

  @JsonProperty("isEntrepreneur")
    @Schema(description = "Trả về true nếu người dùng có hồ sơ doanh nhân.")
    boolean isEntrepreneur,

    @Schema(description = "ID của hồ sơ doanh nhân (nếu có).")
    Long entrepreneurId,

    @Schema(description = "ID của hồ sơ bác sỹ (nếu có).")
    DoctorContextDto doctorContextsDto

) implements Serializable {} 
