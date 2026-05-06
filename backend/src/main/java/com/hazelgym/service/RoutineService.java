package com.hazelgym.service;

import java.util.List;

import com.hazelgym.dto.request.RoutineRequest;
import com.hazelgym.dto.response.RoutineResponse;

public interface RoutineService {

    List<RoutineResponse> findAll();

    RoutineResponse findById(Long id);

    RoutineResponse create(RoutineRequest request);

    RoutineResponse update(Long id, RoutineRequest request);

    void delete(Long id);
}
