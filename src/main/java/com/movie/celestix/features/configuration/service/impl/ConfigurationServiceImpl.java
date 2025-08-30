package com.movie.celestix.features.configuration.service.impl;

import com.movie.celestix.common.models.Configuration;
import com.movie.celestix.common.repository.jpa.ConfigurationJpaRepository;
import com.movie.celestix.features.configuration.dto.ConfigurationResponse;
import com.movie.celestix.features.configuration.dto.UpdateConfigurationRequest;
import com.movie.celestix.features.configuration.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ConfigurationServiceImpl implements ConfigurationService {

    private final ConfigurationJpaRepository configurationJpaRepository;

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
    }

    @Override
    public void saveConfiguration(Configuration configuration) {
        configurationJpaRepository.save(configuration);
    }
}
