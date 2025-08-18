package com.movie.celestix.common.enums;

import java.util.Arrays;

public enum MovieRating {
    G(1L, "G"),
    PG(2L, "PG"),
    PG_13(3L, "PG-13"),
    R(4L, "R"),
    NC_17(5L, "NC-17");

    private final Long id;
    private final String displayName;

    MovieRating(Long id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public Long getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static MovieRating fromId(Long id) {
        return Arrays.stream(MovieRating.values())
                .filter(r -> r.id.equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid MovieRating ID: " + id));
    }
}
