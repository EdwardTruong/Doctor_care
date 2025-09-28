package com.example.doctorcare.application.web.place;

import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.example.doctorcare.application.service.place.command.CreatePlaceCommand;
import com.example.doctorcare.application.service.place.command.UpdatePlaceCommand;
import com.example.doctorcare.application.service.place.dto.PlaceDto;
import com.example.doctorcare.core.cqrs.utils.Page;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import jakarta.validation.Valid;

@RequestMapping("/api/v1/places")
@Tag(name = "[Admin] Thêm vùng", description = "APIs vùng của ông nội supper admin")
public interface PlacesController {

        @PostMapping
        @Operation(summary = "Thêm mới vùng", description = "Thêm vùng")
        ResponseEntity<PlaceDto> createPlace(
                        @Valid @RequestBody CreatePlaceCommand command);

        @PutMapping("/{id}")
        @Operation(summary = "Cập nhật thông vùng", description = "Cập nhật vùng")
        ResponseEntity<PlaceDto> updatePlace(
                        @PathVariable("id") Long placeId,
                        @Valid @RequestBody UpdatePlaceCommand request);

        @GetMapping
        @Operation(summary = "Lấy danh sách vùng", description = "Lấy danh sách vùng")
        ResponseEntity<Page<PlaceDto>> getPlaces(
                        @Parameter(description = "Id Vùng.") @RequestParam(required = false) Long id,
                        @RequestParam(required = false) String keyword,
                        @ParameterObject @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable);

        @GetMapping("/{id}")
        @Operation(summary = "Xóa vùng", description = "Xóa vùng")
        ResponseEntity<PlaceDto> getPlace(
                        @Parameter(description = "ID của vùng") @PathVariable("id") Long placeId);                        

        @DeleteMapping("/{id}")
        @Operation(summary = "Xóa vùng", description = "Xóa vùng")
        ResponseEntity<Void> deletePlace(
                        @Parameter(description = "ID của vùng") @PathVariable("id") Long placeId);

}
