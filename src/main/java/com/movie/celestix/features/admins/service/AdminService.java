package com.movie.celestix.features.admins.service;

import com.movie.celestix.features.admins.dto.AdminResponse;
import com.movie.celestix.features.admins.dto.CreateAdminRequest;
import com.movie.celestix.features.admins.dto.UpdateAdminRequest;

import java.util.List;

public interface AdminService {
    AdminResponse createAdmin(CreateAdminRequest createAdminRequest);
    List<AdminResponse> getAllAdmins();
    AdminResponse getAdminById(Long id);
    AdminResponse updateAdmin(Long id, UpdateAdminRequest updateAdminRequest);
    void deleteAdmin(Long id);
}
