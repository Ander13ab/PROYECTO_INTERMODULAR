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

import com.hazelgym.dto.request.ClassSessionRequest;
import com.hazelgym.dto.response.ClassSessionResponse;
import com.hazelgym.service.ClassSessionService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/class-sessions")
@Tag(name = "Sesiones de clase", description = "Consulta y gestion de sesiones concretas de clase")
@SecurityRequirement(name = "bearerAuth")
public class ClassSessionController {

    private final ClassSessionService classSessionService;

    public ClassSessionController(ClassSessionService classSessionService) {
        this.classSessionService = classSessionService;
    }

    @GetMapping
    @Operation(summary = "Listar sesiones de clase")
    public List<ClassSessionResponse> findAll() {
        return classSessionService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener sesion de clase por id")
    public ClassSessionResponse findById(@PathVariable Long id) {
        return classSessionService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear sesion de clase")
    public ClassSessionResponse create(@Valid @RequestBody ClassSessionRequest request) {
        return classSessionService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar sesion de clase")
    public ClassSessionResponse update(@PathVariable Long id, @Valid @RequestBody ClassSessionRequest request) {
        return classSessionService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar sesion de clase")
    public void delete(@PathVariable Long id) {
        classSessionService.delete(id);
    }
}
