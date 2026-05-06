package com.hazelgym.service;

import java.util.List;

import com.hazelgym.dto.request.MachineRequest;
import com.hazelgym.dto.response.MachineResponse;

public interface MachineService {

    List<MachineResponse> findAll();

    MachineResponse findById(Long id);

    MachineResponse create(MachineRequest request);

    MachineResponse update(Long id, MachineRequest request);

    void delete(Long id);
}
