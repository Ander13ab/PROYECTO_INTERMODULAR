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

import com.hazelgym.dto.request.QrCodeRequest;
import com.hazelgym.dto.response.QrCodeResponse;
import com.hazelgym.service.QrCodeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/qr-codes")
@Tag(name = "Codigos QR", description = "Consulta y gestion de codigos QR del sistema")
@SecurityRequirement(name = "bearerAuth")
public class QrCodeController {

    private final QrCodeService qrCodeService;

    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @GetMapping
    @Operation(summary = "Listar codigos QR")
    public List<QrCodeResponse> findAll() {
        return qrCodeService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener codigo QR por id")
    public QrCodeResponse findById(@PathVariable Long id) {
        return qrCodeService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear codigo QR")
    public QrCodeResponse create(@Valid @RequestBody QrCodeRequest request) {
        return qrCodeService.create(request);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar codigo QR")
    public QrCodeResponse update(@PathVariable Long id, @Valid @RequestBody QrCodeRequest request) {
        return qrCodeService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar codigo QR")
    public void delete(@PathVariable Long id) {
        qrCodeService.delete(id);
    }
}
