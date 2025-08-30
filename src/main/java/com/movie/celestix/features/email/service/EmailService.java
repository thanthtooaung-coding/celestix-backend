package com.movie.celestix.features.email.service;

import com.movie.celestix.features.email.dto.EmailDetails;

public interface EmailService {
    void sendBookingConfirmationEmail(EmailDetails emailDetails);
}
