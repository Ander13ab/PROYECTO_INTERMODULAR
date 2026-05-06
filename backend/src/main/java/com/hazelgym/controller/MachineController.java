package com.hazelgym.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hazelgym.dto.request.MachineRequest;
import com.hazelgym.dto.response.MachineResponse;
import com.hazelgym.service.MachineService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/machines")
public class MachineController {

    private final MachineService machineService;

    public MachineController(MachineService machineService) {
        this.machineService = machineService;
    }

    @GetMapping
    public List<MachineResponse> findAll() {
        return machineService.findAll();
    }

    @GetMapping("/{id}")
    public MachineResponse findById(@PathVariable Long id) {
        return machineService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MachineResponse create(@Valid @RequestBody MachineRequest request) {
        return machineService.create(request);
    }

    @PutMapping("/{id}")
    public MachineResponse update(@PathVariable Long id, @Valid @RequestBody MachineRequest request) {
        return machineService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        machineService.delete(id);
    }
}
