package com.hazelgym.service;

import java.util.List;

import com.hazelgym.dto.request.GymClassRequest;
import com.hazelgym.dto.response.GymClassResponse;

public interface GymClassService {

    List<GymClassResponse> findAll();

    GymClassResponse findById(Long id);

    GymClassResponse create(GymClassRequest request);

    GymClassResponse update(Long id, GymClassRequest request);

    void delete(Long id);
}
