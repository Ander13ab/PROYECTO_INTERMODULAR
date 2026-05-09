package com.hazelgym.service;

import java.util.List;

import com.hazelgym.dto.request.ClassSessionRequest;
import com.hazelgym.dto.response.ClassSessionResponse;

public interface ClassSessionService {

    List<ClassSessionResponse> findAll();

    ClassSessionResponse findById(Long id);

    ClassSessionResponse create(ClassSessionRequest request);

    ClassSessionResponse update(Long id, ClassSessionRequest request);

    void delete(Long id);
}
