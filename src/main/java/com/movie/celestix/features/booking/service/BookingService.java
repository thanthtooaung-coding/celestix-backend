package com.movie.celestix.features.booking.service;

import com.movie.celestix.features.booking.dto.BookingDetailResponse;
import com.movie.celestix.features.booking.dto.BookingResponse;
import com.movie.celestix.features.booking.dto.CreateBookingRequest;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(CreateBookingRequest request, String userEmail);
    List<BookingDetailResponse> retrieveAll();
    void delete(Long id);
}
