package com.hazelgym.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hazelgym.dto.request.RoutineAssignmentRequest;
import com.hazelgym.dto.response.RoutineAssignmentResponse;
import com.hazelgym.service.RoutineAssignmentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/routine-assignments")
@Tag(name = "Asignaciones de rutina", description = "Relacion entre rutinas y clientes")
@SecurityRequirement(name = "bearerAuth")
public class RoutineAssignmentController {

    private final RoutineAssignmentService routineAssignmentService;

    public RoutineAssignmentController(RoutineAssignmentService routineAssignmentService) {
        this.routineAssignmentService = routineAssignmentService;
    }

    @GetMapping
    @Operation(summary = "Listar asignaciones de rutina")
    public List<RoutineAssignmentResponse> findAll() {
        return routineAssignmentService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener asignacion de rutina por id")
    public RoutineAssignmentResponse findById(@PathVariable Long id) {
        return routineAssignmentService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear asignacion de rutina")
    public RoutineAssignmentResponse create(@Valid @RequestBody RoutineAssignmentRequest request) {
        return routineAssignmentService.create(request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar asignacion de rutina")
    public void delete(@PathVariable Long id) {
        routineAssignmentService.delete(id);
    }
}
