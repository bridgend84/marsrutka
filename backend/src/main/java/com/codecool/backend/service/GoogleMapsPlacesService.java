package com.codecool.backend.service;

import com.codecool.backend.model.Place;
import com.codecool.backend.model.PlaceType;
import com.codecool.backend.repository.PlaceRepository;
import com.codecool.backend.repository.PlaceTypeRepository;
import com.google.maps.GeoApiContext;
import com.google.maps.NearbySearchRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.LatLng;
import com.google.maps.model.PlacesSearchResponse;
import com.google.maps.model.PlacesSearchResult;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class GoogleMapsPlacesService {
    private final GeoApiContext context;
    private final PlaceRepository placeRepository;
    private final PlaceTypeRepository placeTypeRepository;

    public GoogleMapsPlacesService(GeoApiContext context,
                                   PlaceRepository placeRepository,
                                   PlaceTypeRepository placeTypeRepository) {
        this.context = context;
        this.placeRepository = placeRepository;
        this.placeTypeRepository = placeTypeRepository;
    }

    public void getNearbyPlaces(double latitude,
                                double longitude,
                                int radius) throws IOException, InterruptedException, ApiException {
        PlacesSearchResponse placesSearchResponse = new NearbySearchRequest(context)
                .location(new LatLng(latitude, longitude))
                .radius(radius)
                .await();
        saveResults(placesSearchResponse.results);
        PlacesSearchResponse nextPageSearchResponse = getNextPageRequest(placesSearchResponse.nextPageToken);
        while (nextPageSearchResponse.nextPageToken != null) {
            saveResults(nextPageSearchResponse.results);
            nextPageSearchResponse = getNextPageRequest(nextPageSearchResponse.nextPageToken);
        }
    }

    // TODO InvalidRequestException without debugging
    public PlacesSearchResponse getNextPageRequest(String token) throws IOException, InterruptedException, ApiException {
        return token == null ? null : new NearbySearchRequest(context).pageToken(token).await();
    }

    private void saveResults(PlacesSearchResult[] results) {
        Set<Place> places = Arrays.stream(results)
                .map(placesSearchResult -> Place.builder()
                        .formattedAddress(placesSearchResult.formattedAddress)
                        .geometry(placesSearchResult.geometry)
                        .name(placesSearchResult.name)
                        .icon(placesSearchResult.icon)
                        .placeId(placesSearchResult.placeId)
                        .rating(placesSearchResult.rating)
                        .placeTypes(placesSearchResult.types == null ? null :
                                Arrays.stream(placesSearchResult.types).map(type -> {
                                            PlaceType currentPlaceType;
                                            if (placeTypeRepository.findById(type).isPresent()) {
                                                currentPlaceType = placeTypeRepository.findById(type).get();
                                            } else {
                                                currentPlaceType = placeTypeRepository.save(
                                                        PlaceType.builder()
                                                                .type(type)
                                                                .build());
                                            }
                                            return currentPlaceType;
                                        }
                                ).collect(Collectors.toSet()))
                        .openingHours(placesSearchResult.openingHours)
                        .photos(placesSearchResult.photos == null ? null :
                                Arrays.stream(placesSearchResult.photos).collect(Collectors.toSet()))
                        .vicinity(placesSearchResult.vicinity)
                        .permanentlyClosed(placesSearchResult.permanentlyClosed)
                        .userRatingsTotal(placesSearchResult.userRatingsTotal)
                        .businessStatus(placesSearchResult.businessStatus)
                        .build())
                .collect(Collectors.toSet());
        placeRepository.saveAll(places);
    }

    @PreDestroy
    private void contextShutdown() {
        context.shutdown();
    }
}
