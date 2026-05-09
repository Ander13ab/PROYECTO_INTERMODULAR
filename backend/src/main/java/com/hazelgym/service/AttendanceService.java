package com.hazelgym.service;

import java.util.List;

import com.hazelgym.dto.request.AttendanceRequest;
import com.hazelgym.dto.response.AttendanceResponse;

public interface AttendanceService {

    List<AttendanceResponse> findAll();

    AttendanceResponse findById(Long id);

    AttendanceResponse create(AttendanceRequest request);

    void delete(Long id);
}
