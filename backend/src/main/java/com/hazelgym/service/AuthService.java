package com.hazelgym.service;

import com.hazelgym.dto.request.LoginRequest;
import com.hazelgym.dto.request.RegisterRequest;
import com.hazelgym.dto.response.AuthResponse;
import com.hazelgym.dto.response.AuthenticatedUserResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthenticatedUserResponse me();
}
