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

import com.hazelgym.dto.request.MembershipFeeRequest;
import com.hazelgym.dto.response.MembershipFeeResponse;
import com.hazelgym.service.MembershipFeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/membership-fees")
@Tag(name = "Cuotas", description = "Consulta y gestion de cuotas del gimnasio")
@SecurityRequirement(name = "bearerAuth")
public class MembershipFeeController {

    private final MembershipFeeService membershipFeeService;

    public MembershipFeeController(MembershipFeeService membershipFeeService) {
        this.membershipFeeService = membershipFeeService;
    }

    @GetMapping
    @Operation(summary = "Listar cuotas")
    public List<MembershipFeeResponse> findAll() {
        return membershipFeeService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cuota por id")
    public MembershipFeeResponse findById(@PathVariable Long id) {
        return membershipFeeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear cuota")
    public MembershipFeeResponse create(@Valid @RequestBody MembershipFeeRequest request) {
        return membershipFeeService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar cuota")
    public MembershipFeeResponse update(@PathVariable Long id, @Valid @RequestBody MembershipFeeRequest request) {
        return membershipFeeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar cuota")
    public void delete(@PathVariable Long id) {
        membershipFeeService.delete(id);
    }
}
