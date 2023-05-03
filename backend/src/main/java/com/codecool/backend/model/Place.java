package com.codecool.backend.model;

import com.google.maps.model.*;
import jakarta.persistence.*;
import lombok.*;

import java.net.URL;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Place {
    public String formattedAddress;
    public Geometry geometry;
    public String name;
    public URL icon;
    @Id
    public String placeId;
    public float rating;
    @ManyToMany
    @JoinTable(
            name = "place_by_place_types",
            joinColumns = @JoinColumn(name = "place_id"),
            inverseJoinColumns = @JoinColumn(name = "type")
    )
    public Set<PlaceType> placeTypes;
    public OpeningHours openingHours;
    @ElementCollection
    public Set<Photo> photos;
    public String vicinity;
    public boolean permanentlyClosed;
    public int userRatingsTotal;
    public String businessStatus;
}
