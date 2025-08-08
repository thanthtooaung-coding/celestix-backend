package com.movie.celestix.features.auth.dto;

import com.movie.celestix.common.enums.Role;
import lombok.Data;

public record RegisterRequest (
    String email,
    String password,
    String phoneNumber,
    Role role
) {}
