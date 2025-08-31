package com.movie.celestix.features.jobs;

import com.movie.celestix.common.models.BookedSeat;
import com.movie.celestix.common.models.Booking;
import com.movie.celestix.common.models.Showtime;
import com.movie.celestix.common.repository.jpa.ShowtimeJpaRepository;
import com.movie.celestix.features.email.dto.ReminderEmailDetails;
import com.movie.celestix.features.email.service.EmailService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ShowtimeReminderJob extends QuartzJobBean {

    private static final Logger logger = LoggerFactory.getLogger(ShowtimeReminderJob.class);
    private final ShowtimeJpaRepository showtimeJpaRepository;
    private final EmailService emailService;

    @Override
    @Transactional(readOnly = true)
    protected void executeInternal(@NonNull JobExecutionContext context) {
        final LocalTime now = LocalTime.now();
        final LocalTime tenMinutesFromNow = now.plusMinutes(10);
        logger.info("Running ShowtimeReminderJob at {} for showtimes between {} and {}", now, now, tenMinutesFromNow);

        final List<Showtime> upcomingShowtimes = showtimeJpaRepository.findByShowtimeDateAndShowtimeTimeBetween(
                LocalDate.now(), now, tenMinutesFromNow
        );

        if (CollectionUtils.isEmpty(upcomingShowtimes)) {
            logger.info("No upcoming showtimes found in the next 10 minutes.");
            return;
        }

        for (Showtime showtime : upcomingShowtimes) {
            logger.info("Processing showtime for movie: {} at theater: {}", showtime.getMovie().getTitle(), showtime.getTheater().getName());

            Map<Booking, List<BookedSeat>> bookingsMap = showtime.getBookedSeats().stream()
                    .collect(Collectors.groupingBy(BookedSeat::getBooking));

            for (Booking booking : bookingsMap.keySet()) {
                ReminderEmailDetails details = new ReminderEmailDetails(
                        booking.getUser().getName(),
                        booking.getUser().getEmail(),
                        showtime.getMovie().getTitle(),
                        showtime.getMovie().getMoviePosterUrl(),
                        showtime.getTheater().getName(),
                        showtime.getShowtimeTime()
                );
                emailService.sendShowtimeReminderEmail(details);
            }
        }
    }
}
