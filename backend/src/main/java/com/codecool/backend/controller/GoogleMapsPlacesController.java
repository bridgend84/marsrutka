package com.codecool.backend.controller;

import com.codecool.backend.service.GoogleMapsPlacesService;
import com.google.maps.errors.ApiException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GoogleMapsPlacesController {
    private final GoogleMapsPlacesService googleMapsPlacesService;

    public GoogleMapsPlacesController(GoogleMapsPlacesService googleMapsPlacesService) {
        this.googleMapsPlacesService = googleMapsPlacesService;
    }

    @GetMapping("/places")
    public void getPlaces(@RequestParam double latitude,
                          @RequestParam double longitude,
                          @RequestParam int radius) throws IOException, InterruptedException, ApiException {
        googleMapsPlacesService.getNearbyPlaces(latitude, longitude, radius);
    }
}
