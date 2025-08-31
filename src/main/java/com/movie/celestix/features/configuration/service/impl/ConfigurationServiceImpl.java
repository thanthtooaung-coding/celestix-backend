package com.movie.celestix.features.configuration.service.impl;

import com.movie.celestix.common.models.Configuration;
import com.movie.celestix.common.repository.jpa.ConfigurationJpaRepository;
import com.movie.celestix.features.configuration.dto.ConfigurationResponse;
import com.movie.celestix.features.configuration.dto.UpdateConfigurationRequest;
import com.movie.celestix.features.configuration.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationJpaRepository configurationJpaRepository;
    private final Scheduler scheduler;

    @Override
    @Transactional(readOnly = true)
    public ConfigurationResponse getConfigurationByCode(String code) {
        Configuration config = configurationJpaRepository.findByCode(code)
                .orElseGet(() -> {
                    Configuration newConfig = new Configuration(code, "5");
                    return configurationJpaRepository.save(newConfig);
                });
        return new ConfigurationResponse(config.getCode(), config.getValue());
    }

    @Override
    @Transactional
    public void updateConfiguration(String code, UpdateConfigurationRequest request) {
        Configuration config = configurationJpaRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Configuration with code " + code + " not found"));
        config.setValue(request.value());
        configurationJpaRepository.save(config);

        if ("SHOWTIME_SCHEDULER_MINUTES".equals(code)) {
            rescheduleShowtimeReminderJob(Integer.parseInt(request.value()));
        }
    }

    @Override
    public void saveConfiguration(Configuration configuration) {
        configurationJpaRepository.save(configuration);
    }

    private void rescheduleShowtimeReminderJob(int minutes) {
        try {
            final TriggerKey triggerKey = new TriggerKey("showtimeReminderTrigger");
            final CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
            final String cronExpression = "0 0/" + minutes + " * * * ?";
            if (trigger == null || !trigger.getCronExpression().equals(cronExpression)) {
                final CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(cronExpression);
                final Trigger newTrigger = TriggerBuilder.newTrigger()
                        .forJob("showtimeReminderJob")
                        .withIdentity(triggerKey)
                        .withSchedule(scheduleBuilder)
                        .build();
                if(trigger == null){
                    scheduler.scheduleJob(newTrigger);
                }else{
                    scheduler.rescheduleJob(triggerKey, newTrigger);
                }

            }
        } catch (SchedulerException e) {
            throw new RuntimeException("Could not reschedule job", e);
        }
    }
}
