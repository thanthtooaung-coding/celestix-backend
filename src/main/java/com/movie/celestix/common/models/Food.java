package com.movie.celestix.common.models;

import com.movie.celestix.common.enums.Category;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Food extends MasterData {
    private String name;

    @Enumerated(EnumType.STRING)
    private Category category;

    private double price;
    private String allergens;
    private String description;

}
