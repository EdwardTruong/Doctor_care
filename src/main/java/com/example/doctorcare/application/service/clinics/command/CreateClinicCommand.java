package com.example.doctorcare.application.service.clinics.command;

import com.example.doctorcare.domain.business.places.Places;

public record CreateClinicCommand(

        String name,
        String phone,
        String address,
        String introductionHTML,
        String introductionMarkdown,
        String description,
        Places place) {

}
