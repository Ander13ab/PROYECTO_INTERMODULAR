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

import com.hazelgym.dto.request.RoutineRequest;
import com.hazelgym.dto.response.RoutineResponse;
import com.hazelgym.service.RoutineService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/routines")
@Tag(name = "Rutinas", description = "Consulta y gestion de rutinas de entrenamiento")
@SecurityRequirement(name = "bearerAuth")
public class RoutineController {

    private final RoutineService routineService;

    public RoutineController(RoutineService routineService) {
        this.routineService = routineService;
    }

    @GetMapping
    @Operation(summary = "Listar rutinas")
    public List<RoutineResponse> findAll() {
        return routineService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener rutina por id")
    public RoutineResponse findById(@PathVariable Long id) {
        return routineService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear rutina")
    public RoutineResponse create(@Valid @RequestBody RoutineRequest request) {
        return routineService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar rutina")
    public RoutineResponse update(@PathVariable Long id, @Valid @RequestBody RoutineRequest request) {
        return routineService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar rutina")
    public void delete(@PathVariable Long id) {
        routineService.delete(id);
    }
}
