package com.example.doctorcare.core.enums_NotUsedYet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum to represent gender.
 */
@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("Nam"),
    FEMALE("Nữ"),
    OTHER("Khác");

    private final String description;
}