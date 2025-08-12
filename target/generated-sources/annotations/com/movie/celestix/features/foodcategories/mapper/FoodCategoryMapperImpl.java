package com.movie.celestix.features.foodcategories.mapper;

import com.movie.celestix.common.models.FoodCategory;
import com.movie.celestix.features.foodcategories.dto.FoodCategoryResponse;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-12T13:48:32+0700",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.7 (Ubuntu)"
)
@Component
public class FoodCategoryMapperImpl implements FoodCategoryMapper {

    @Override
    public FoodCategoryResponse toDto(FoodCategory foodCategory) {
        if ( foodCategory == null ) {
            return null;
        }

        Long id = null;
        String name = null;

        id = foodCategory.getId();
        name = foodCategory.getName();

        String response = null;

        FoodCategoryResponse foodCategoryResponse = new FoodCategoryResponse( id, name, response );

        return foodCategoryResponse;
    }

    @Override
    public List<FoodCategoryResponse> toDtoList(List<FoodCategory> categories) {
        if ( categories == null ) {
            return null;
        }

        List<FoodCategoryResponse> list = new ArrayList<FoodCategoryResponse>( categories.size() );
        for ( FoodCategory foodCategory : categories ) {
            list.add( toDto( foodCategory ) );
        }

        return list;
    }
}
