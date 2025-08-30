package com.movie.celestix.features.booking.service.impl;

import com.movie.celestix.common.enums.BookingStatus;
import com.movie.celestix.common.enums.PaymentStatus;
import com.movie.celestix.common.models.*;
import com.movie.celestix.common.repository.jpa.*;
import com.movie.celestix.common.util.CreditCardValidator;
import com.movie.celestix.features.booking.dto.BookingDetailResponse;
import com.movie.celestix.features.booking.dto.BookingResponse;
import com.movie.celestix.features.booking.dto.CreateBookingRequest;
import com.movie.celestix.features.booking.mapper.BookingMapper;
import com.movie.celestix.features.booking.service.BookingService;
import com.movie.celestix.features.email.dto.EmailDetails;
import com.movie.celestix.features.email.service.EmailService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    private final ConfigurationJpaRepository configurationJpaRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public BookingResponse createBooking(final CreateBookingRequest request, final String userEmail) {
        if (request.cardDetails() == null) {
            throw new IllegalArgumentException("Payment details are required.");
        }
        if (!CreditCardValidator.isValid(request.cardDetails().cardNumber())) {
            throw new IllegalArgumentException("Invalid credit card number provided.");
        }

        final User user = userJpaRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        final Configuration config = configurationJpaRepository.findByCode("MAX_BOOKINGS_PER_USER")
                .orElse(new Configuration("MAX_BOOKINGS_PER_USER", "10"));
        final int maxBookings = Integer.parseInt(config.getValue());

        if (request.seatNumbers().size() >= maxBookings) {
            throw new IllegalStateException("You have reached the maximum booking limit of " + maxBookings + ".");
        }

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
//        booking.setPaymentStatus(PaymentStatus.SUCCESS);

        final Set<BookedSeat> bookedSeats = new HashSet<>();
        BigDecimal totalPrice = BigDecimal.ZERO;
        for (String seatNumber : request.seatNumbers()) {
            final BookedSeat bookedSeat = new BookedSeat();
            BigDecimal price = calculatePrice(showtime.getTheater(), seatNumber);
            totalPrice = totalPrice.add(price);
            bookedSeat.setBooking(booking);
            bookedSeat.setShowtime(showtime);
            bookedSeat.setSeatNumber(seatNumber);
            bookedSeat.setPrice(price);
            bookedSeats.add(bookedSeat);
        }

        booking.setBookedSeats(bookedSeats);
        booking.setTotalPrice(totalPrice);

        String header = "CELESTIX-"+booking.getUser().getName()+request.cardDetails().cardNumber().substring(request.cardDetails().cardNumber().length() - 4);


        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("PAYMENT-INITIATOR", header.toString());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> body = new HashMap<>();
        body.put("amount", totalPrice);
        body.put("currency", "USD");
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, httpHeaders);
        
        RestTemplate restTemplate = new RestTemplate();
        String paymentUrl = "https://aungkhaingkhant.online/test-bank/api/make-payment";
        Map<String, Object> paymentResponse = restTemplate.postForObject(paymentUrl, entity, Map.class);

        if (!paymentResponse.get("status").equals("SUCCESS")) {
            booking.setPaymentStatus(PaymentStatus.FAIL);
        }else{
            booking.setPaymentStatus(PaymentStatus.SUCCESS);
        }
        showtime.setSeatsAvailable(showtime.getSeatsAvailable() - request.seatNumbers().size());

        final Booking savedBooking = bookingJpaRepository.save(booking);
        showtimeJpaRepository.save(showtime);

        if (savedBooking.getPaymentStatus() == PaymentStatus.SUCCESS) {
            final String seats = savedBooking.getBookedSeats().stream()
                    .map(BookedSeat::getSeatNumber)
                    .sorted()
                    .collect(Collectors.joining(", "));

            final EmailDetails emailDetails = new EmailDetails(
                    savedBooking.getUser().getName(),
                    savedBooking.getUser().getEmail(),
                    savedBooking.getBookingId(),
                    showtime.getMovie().getTitle(),
                    showtime.getTheater().getName(),
                    showtime.getShowtimeDate().format(DateTimeFormatter.ofPattern("EEE, dd MMM yyyy")),
                    showtime.getShowtimeTime().format(DateTimeFormatter.ofPattern("hh:mm a")),
                    seats,
                    savedBooking.getTotalPrice()
            );
            emailService.sendBookingConfirmationEmail(emailDetails);
        }

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

    @Override
    @Transactional(readOnly = true)
    public List<BookingDetailResponse> retrieveAll() {
        List<Booking> bookings = bookingJpaRepository.findAll();
        return bookingMapper.toDetailDtoList(bookings);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        final Booking booking = bookingJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Booking with id " + id + " not found"));

        if (!booking.getBookedSeats().isEmpty()) {
            final Showtime showtime = booking.getBookedSeats().iterator().next().getShowtime();
            showtime.setSeatsAvailable(showtime.getSeatsAvailable() + booking.getBookedSeats().size());
            showtimeJpaRepository.save(showtime);
        }

        bookingJpaRepository.deleteById(id);
    }
}