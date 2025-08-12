package com.movie.celestix.features.moviegenres.mapper;

import com.movie.celestix.common.models.MovieGenre;
import com.movie.celestix.features.moviegenres.dto.MovieGenreResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-12T14:12:36+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Ubuntu)"
)
@Component
public class MovieGenreMapperImpl implements MovieGenreMapper {

    @Override
    public MovieGenreResponse toDto(MovieGenre movieGenre) {
        if ( movieGenre == null ) {
            return null;
        }

        Long id = null;
        String name = null;
        String description = null;

        id = movieGenre.getId();
        name = movieGenre.getName();
        description = movieGenre.getDescription();

        MovieGenreResponse movieGenreResponse = new MovieGenreResponse( id, name, description );

        return movieGenreResponse;
    }

    @Override
    public List<MovieGenreResponse> toDtoList(List<MovieGenre> genres) {
        if ( genres == null ) {
            return null;
        }

        List<MovieGenreResponse> list = new ArrayList<MovieGenreResponse>( genres.size() );
        for ( MovieGenre movieGenre : genres ) {
            list.add( toDto( movieGenre ) );
        }

        return list;
    }
}
