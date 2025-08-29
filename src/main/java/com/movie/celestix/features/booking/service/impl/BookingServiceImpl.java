package com.movie.celestix.features.booking.service.impl;

import com.movie.celestix.common.enums.BookingStatus;
import com.movie.celestix.common.enums.PaymentStatus;
import com.movie.celestix.common.models.*;
import com.movie.celestix.common.repository.jpa.BookedSeatJpaRepository;
import com.movie.celestix.common.repository.jpa.BookingJpaRepository;
import com.movie.celestix.common.repository.jpa.ShowtimeJpaRepository;
import com.movie.celestix.common.repository.jpa.UserJpaRepository;
import com.movie.celestix.features.booking.dto.BookingResponse;
import com.movie.celestix.features.booking.dto.CreateBookingRequest;
import com.movie.celestix.features.booking.mapper.BookingMapper;
import com.movie.celestix.features.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingJpaRepository bookingJpaRepository;
    private final ShowtimeJpaRepository showtimeJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final BookedSeatJpaRepository bookedSeatJpaRepository;
    private final BookingMapper bookingMapper;

    @Override
    @Transactional
    public BookingResponse createBooking(final CreateBookingRequest request, final String userEmail) {
        final User user = userJpaRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        final Showtime showtime = showtimeJpaRepository.findById(request.showtimeId())
                .orElseThrow(() -> new RuntimeException("Showtime not found"));

        final List<String> alreadyBookedSeats = bookedSeatJpaRepository.findByShowtimeId(showtime.getId())
                .stream()
                .map(BookedSeat::getSeatNumber)
                .toList();

        for (String seatNumber : request.seatNumbers()) {
            if (alreadyBookedSeats.contains(seatNumber)) {
                throw new IllegalStateException("Seat " + seatNumber + " is already booked.");
            }
        }

        final Booking booking = new Booking();
        booking.setBookingId(UUID.randomUUID().toString());
        booking.setUser(user);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setPaymentStatus(PaymentStatus.SUCCESS);

        final Set<BookedSeat> bookedSeats = new HashSet<>();
        for (String seatNumber : request.seatNumbers()) {
            final BookedSeat bookedSeat = new BookedSeat();
            bookedSeat.setBooking(booking);
            bookedSeat.setShowtime(showtime);
            bookedSeat.setSeatNumber(seatNumber);
            bookedSeat.setPrice(calculatePrice(showtime.getTheater(), seatNumber));
            bookedSeats.add(bookedSeat);
        }

        booking.setBookedSeats(bookedSeats);
        showtime.setSeatsAvailable(showtime.getSeatsAvailable() - request.seatNumbers().size());

        bookingJpaRepository.save(booking);
        showtimeJpaRepository.save(showtime);

        return bookingMapper.toDto(booking);
    }

    /**
     * Calculates the price of a seat based on its row and the theater's pricing configuration.
     * This logic mirrors the frontend's seat type determination.
     *
     * @param theater The theater where the showtime is happening.
     * @param seatNumber The seat identifier, e.g., "A5", "C12".
     * @return The price of the seat as a BigDecimal.
     */
    private BigDecimal calculatePrice(final Theater theater, final String seatNumber) {
        if (seatNumber == null || seatNumber.isEmpty() || !Character.isLetter(seatNumber.charAt(0))) {
            throw new IllegalArgumentException("Invalid seat number format: " + seatNumber);
        }

        final char rowChar = Character.toUpperCase(seatNumber.charAt(0));
        final int rowIndex = rowChar - 'A';

        final int premiumRows = theater.getPremiumSeat().getTotalRows();
        final int regularRows = theater.getRegularSeat().getTotalRows();
        final int economyRows = theater.getEconomySeat().getTotalRows();

        if (rowIndex < premiumRows) {
            return theater.getPremiumSeat().getTotalPrice();
        } else if (rowIndex < premiumRows + regularRows) {
            return theater.getRegularSeat().getTotalPrice();
        } else if (rowIndex < premiumRows + regularRows + economyRows) {
            return theater.getEconomySeat().getTotalPrice();
        } else {
            return theater.getBasicSeat().getTotalPrice();
        }
    }
}