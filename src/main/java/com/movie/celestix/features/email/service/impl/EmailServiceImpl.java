package com.movie.celestix.features.email.service.impl;

import com.movie.celestix.features.email.dto.EmailDetails;
import com.movie.celestix.features.email.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Override
    @Async
    public void sendBookingConfirmationEmail(EmailDetails emailDetails) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

            Context context = new Context();
            context.setVariable("customerName", emailDetails.customerName());
            context.setVariable("bookingId", emailDetails.bookingId());
            context.setVariable("movieTitle", emailDetails.movieTitle());
            context.setVariable("theaterName", emailDetails.theaterName());
            context.setVariable("showtimeDate", emailDetails.showtimeDate());
            context.setVariable("showtimeTime", emailDetails.showtimeTime());
            context.setVariable("seats", emailDetails.seats());
            context.setVariable("totalAmount", emailDetails.totalAmount());

            String htmlContent = templateEngine.process("booking-confirmation", context);

            helper.setTo(emailDetails.customerEmail());
            helper.setFrom(mailFrom);
            helper.setSubject("Your Celestix Booking Confirmation for " + emailDetails.movieTitle());
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            logger.info("Booking confirmation email sent successfully to {}", emailDetails.customerEmail());
        } catch (MessagingException e) {
            logger.error("Failed to send booking confirmation email to {}", emailDetails.customerEmail(), e);
        }
    }
}
