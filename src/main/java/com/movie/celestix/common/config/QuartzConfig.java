package com.movie.celestix.common.config;

import com.movie.celestix.common.repository.jpa.ConfigurationJpaRepository;
import com.movie.celestix.features.jobs.ShowtimeReminderJob;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class QuartzConfig {

    private final ConfigurationJpaRepository configurationJpaRepository;

    @Bean
    public JobDetail showtimeReminderJobDetail() {
        return JobBuilder.newJob(ShowtimeReminderJob.class)
                .withIdentity("showtimeReminderJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger showtimeReminderJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(showtimeReminderJobDetail())
                .withIdentity("showtimeReminderTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule(getCronExpressionFromDB()))
                .build();
    }

    private String getCronExpressionFromDB() {
        final com.movie.celestix.common.models.Configuration config = configurationJpaRepository.findByCode("SHOWTIME_SCHEDULER_MINUTES")
                .orElse(new com.movie.celestix.common.models.Configuration("SHOWTIME_SCHEDULER_MINUTES", "10"));
        final String minutes = config.getValue();
        return "0 0/" + minutes + " * * * ?";
    }
}
