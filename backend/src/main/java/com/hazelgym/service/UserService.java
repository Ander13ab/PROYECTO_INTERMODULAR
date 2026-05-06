package com.hazelgym.service;

import java.util.List;

import com.hazelgym.dto.request.UserCreateRequest;
import com.hazelgym.dto.request.UserUpdateRequest;
import com.hazelgym.dto.response.UserResponse;

public interface UserService {

    List<UserResponse> findAll();

    UserResponse findById(Long id);

    UserResponse create(UserCreateRequest request);

    UserResponse update(Long id, UserUpdateRequest request);

    void delete(Long id);
}
