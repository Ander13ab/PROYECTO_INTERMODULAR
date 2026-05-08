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

import com.hazelgym.dto.request.GymClassRequest;
import com.hazelgym.dto.response.GymClassResponse;
import com.hazelgym.service.GymClassService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/classes")
@Tag(name = "Clases", description = "Consulta y gestion de clases dirigidas")
@SecurityRequirement(name = "bearerAuth")
public class GymClassController {

    private final GymClassService gymClassService;

    public GymClassController(GymClassService gymClassService) {
        this.gymClassService = gymClassService;
    }

    @GetMapping
    @Operation(summary = "Listar clases")
    public List<GymClassResponse> findAll() {
        return gymClassService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener clase por id")
    public GymClassResponse findById(@PathVariable Long id) {
        return gymClassService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear clase")
    public GymClassResponse create(@Valid @RequestBody GymClassRequest request) {
        return gymClassService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar clase")
    public GymClassResponse update(@PathVariable Long id, @Valid @RequestBody GymClassRequest request) {
        return gymClassService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar clase")
    public void delete(@PathVariable Long id) {
        gymClassService.delete(id);
    }
}
