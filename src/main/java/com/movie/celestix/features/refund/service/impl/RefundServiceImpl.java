package com.movie.celestix.features.refund.service.impl;

import com.movie.celestix.common.enums.BookingStatus;
import com.movie.celestix.common.enums.RefundStatus;
import com.movie.celestix.common.models.Booking;
import com.movie.celestix.common.models.BookingRefundRecord;
import com.movie.celestix.common.models.User;
import com.movie.celestix.common.repository.jpa.BookingJpaRepository;
import com.movie.celestix.common.repository.jpa.BookingRefundRecordJpaRepository;
import com.movie.celestix.common.repository.jpa.UserJpaRepository;
import com.movie.celestix.features.refund.dto.RefundResponse;
import com.movie.celestix.features.refund.dto.UpdateRefundStatusRequest;
import com.movie.celestix.features.refund.mapper.RefundMapper;
import com.movie.celestix.features.refund.service.RefundService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RefundServiceImpl implements RefundService {

    private final BookingRefundRecordJpaRepository refundRepository;
    private final BookingJpaRepository bookingJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final RefundMapper refundMapper;


    @Override
    @Transactional
    public void requestRefund(Long bookingId, String userEmail) {
        Booking booking = bookingJpaRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        if (!booking.getUser().getEmail().equals(userEmail)) {
            throw new IllegalStateException("You are not authorized to request a refund for this booking.");
        }

        if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
            throw new IllegalStateException("This booking has already been confirmed.");
        }

        refundRepository.findByBookingId(bookingId).ifPresent(r -> {
            throw new IllegalStateException("A refund request for this booking has already been submitted.");
        });

        BookingRefundRecord refundRecord = new BookingRefundRecord();
        refundRecord.setBooking(booking);
        refundRecord.setStatus(RefundStatus.PENDING);
        refundRepository.save(refundRecord);
    }

    @Override
    @Transactional
    public void processRefund(Long refundId, UpdateRefundStatusRequest request, String adminEmail) {
        BookingRefundRecord refundRecord = refundRepository.findById(refundId)
                .orElseThrow(() -> new RuntimeException("Refund request not found"));

        User admin = userJpaRepository.findByEmail(adminEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Admin not found"));

        refundRecord.setApprovedBy(admin);
        refundRecord.setStatus(request.status());

        if (request.status() == RefundStatus.APPROVED) {
            Booking booking = refundRecord.getBooking();
            booking.setBookingStatus(BookingStatus.CANCELLED);
            bookingJpaRepository.save(booking);
        }

        refundRepository.save(refundRecord);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RefundResponse> getAllRefunds() {
        return refundMapper.toDtoList(refundRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public RefundResponse getRefundById(Long id) {
        BookingRefundRecord refundRecord = refundRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Refund request not found"));
        return refundMapper.toDto(refundRecord);
    }
}
