package com.hazelgym.service;

import java.util.List;

import com.hazelgym.dto.request.RoutineAssignmentRequest;
import com.hazelgym.dto.response.RoutineAssignmentResponse;

public interface RoutineAssignmentService {

    List<RoutineAssignmentResponse> findAll();

    RoutineAssignmentResponse findById(Long id);

    RoutineAssignmentResponse create(RoutineAssignmentRequest request);

    void delete(Long id);
}
