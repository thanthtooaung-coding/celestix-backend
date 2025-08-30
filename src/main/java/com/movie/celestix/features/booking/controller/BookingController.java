package com.movie.celestix.features.booking.controller;

import com.movie.celestix.common.dto.ApiResponse;
import com.movie.celestix.features.booking.dto.BookingDetailResponse;
import com.movie.celestix.features.booking.dto.BookingResponse;
import com.movie.celestix.features.booking.dto.CreateBookingRequest;
import com.movie.celestix.features.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @RequestBody CreateBookingRequest request,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        BookingResponse booking = bookingService.createBooking(request, userDetails.getUsername());
        return ApiResponse.created(booking, "Booking created successfully");
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BookingDetailResponse>>> retrieveAll() {
        return ApiResponse.ok(bookingService.retrieveAll(), "Bookings retrieved successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable final Long id) {
        bookingService.delete(id);
        return ApiResponse.noContent("Booking deleted successfully");
    }
}
