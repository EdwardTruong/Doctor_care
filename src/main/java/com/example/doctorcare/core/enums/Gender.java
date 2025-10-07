package com.example.doctorcare.core.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum to represent gender.
 */
@Getter
public enum Gender {
    MALE("Nam"),
    FEMALE("Nữ"),
    OTHER("Khác");

    private final String description;

    Gender(String description) {
        this.description = description;
    }
}