package com.movie.celestix.features.booking.service;

import com.movie.celestix.features.booking.dto.BookingResponse;
import com.movie.celestix.features.booking.dto.CreateBookingRequest;

public interface BookingService {
    BookingResponse createBooking(CreateBookingRequest request, String userEmail);
}
