package com.backend.seabook.service;

import com.backend.seabook.dto.request.LoginRequest;
import com.backend.seabook.dto.request.RegisterRequest;
import com.backend.seabook.dto.response.LoginResponse;
import com.backend.seabook.dto.response.RefreshTokenResponse;
import com.backend.seabook.dto.response.RegisterResponse;

public interface AuthenticationService {
    RegisterResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
    RefreshTokenResponse refreshToken();
}
