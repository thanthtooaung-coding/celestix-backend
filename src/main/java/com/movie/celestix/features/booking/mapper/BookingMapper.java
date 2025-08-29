package com.movie.celestix.features.booking.mapper;

import com.movie.celestix.common.models.BookedSeat;
import com.movie.celestix.common.models.Booking;
import com.movie.celestix.features.booking.dto.BookedSeatDto;
import com.movie.celestix.features.booking.dto.BookingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(source = "user.id", target = "userId")
    BookingResponse toDto(Booking booking);

    BookedSeatDto toDto(BookedSeat bookedSeat);
}
