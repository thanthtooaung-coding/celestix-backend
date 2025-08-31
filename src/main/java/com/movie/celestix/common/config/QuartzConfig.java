package com.movie.celestix.common.config;

import com.movie.celestix.features.jobs.ShowtimeReminderJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QuartzConfig {

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
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/10 * * * ?"))
                .build();
    }
}
