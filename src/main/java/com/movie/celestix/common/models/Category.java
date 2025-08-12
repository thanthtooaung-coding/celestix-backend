package com.movie.celestix.common.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Category extends MasterData {
    private String name;

    private String description;
}
