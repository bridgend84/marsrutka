package com.codecool.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class PlaceType {
    @Id
    private String type;
    @ManyToMany
    @JoinTable(
            name = "place_by_place_types",
            joinColumns = @JoinColumn(name = "type"),
            inverseJoinColumns = @JoinColumn(name = "place_id")
    )
    private Set<Place> places;
}
