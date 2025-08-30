package com.movie.celestix.features.configuration.controller;

import com.movie.celestix.common.dto.ApiResponse;
import com.movie.celestix.features.configuration.dto.ConfigurationResponse;
import com.movie.celestix.features.configuration.dto.UpdateConfigurationRequest;
import com.movie.celestix.features.configuration.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/configurations")
@RequiredArgsConstructor
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @GetMapping("/{code}")
    public ResponseEntity<ApiResponse<ConfigurationResponse>> getConfiguration(@PathVariable String code) {
        return ApiResponse.ok(configurationService.getConfigurationByCode(code), "Configuration retrieved successfully");
    }

    @PutMapping("/{code}")
    public ResponseEntity<ApiResponse<Void>> updateConfiguration(@PathVariable String code, @RequestBody UpdateConfigurationRequest request) {
        configurationService.updateConfiguration(code, request);
        return ApiResponse.ok(null, "Configuration updated successfully");
    }
}
