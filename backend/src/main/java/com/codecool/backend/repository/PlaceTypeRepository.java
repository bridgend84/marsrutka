package com.codecool.backend.repository;

import com.codecool.backend.model.PlaceType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceTypeRepository extends JpaRepository<PlaceType, String> {
}
