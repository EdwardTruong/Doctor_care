package com.example.doctorcare.application.service.user.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;


@Builder(toBuilder = true)
@Schema(description = "DTO chứa thông tin chi tiết về tất cả các vai trò và mối quan hệ của người dùng trong hệ thống.")
public record UserContext(

  UserSimpleDto user,

  @JsonProperty("isDoctor")
    @Schema(description = "Trả về true nếu người dùng có hồ sơ doanh nhân.")
    boolean isDoctor,

    @Schema(description = "ID của hồ sơ bác sỹ (nếu có).")
    Long doctorContextsDto,

    // Update cho version 2.0
    @JsonProperty("isInvestmentGroup")
    @Schema(description = "Trả về true nếu là người dùng là người thuộc 1 tổ chức đầu tư.")
    boolean isInvestmentGroup,

    // Update cho version 2.0
    @JsonProperty("isInvestor")
    @Schema(description = "Trả về true nếu là người dùng là người đầu tư đơn lẽ.")
    boolean isInvestor

) implements Serializable {} 
