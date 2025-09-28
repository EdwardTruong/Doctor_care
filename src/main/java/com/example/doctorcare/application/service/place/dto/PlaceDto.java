package com.example.doctorcare.application.service.place.dto;

import com.example.doctorcare.domain.business.places.Places;

public record PlaceDto(

    String name
    // ...... more field 

) {
    public static PlaceDto form(Places places){
        return new PlaceDto(places.getName());
    } 
}
