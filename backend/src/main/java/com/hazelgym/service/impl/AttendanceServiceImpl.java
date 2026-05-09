package com.hazelgym.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hazelgym.dto.request.AttendanceRequest;
import com.hazelgym.dto.response.AttendanceResponse;
import com.hazelgym.exception.ResourceNotFoundException;
import com.hazelgym.model.Attendance;
import com.hazelgym.model.QrCode;
import com.hazelgym.model.User;
import com.hazelgym.repository.AttendanceRepository;
import com.hazelgym.repository.QrCodeRepository;
import com.hazelgym.repository.UserRepository;
import com.hazelgym.service.AttendanceService;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final UserRepository userRepository;
    private final QrCodeRepository qrCodeRepository;

    public AttendanceServiceImpl(
            AttendanceRepository attendanceRepository,
            UserRepository userRepository,
            QrCodeRepository qrCodeRepository
    ) {
        this.attendanceRepository = attendanceRepository;
        this.userRepository = userRepository;
        this.qrCodeRepository = qrCodeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttendanceResponse> findAll() {
        return attendanceRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public AttendanceResponse findById(Long id) {
        return toResponse(getById(id));
    }

    @Override
    public AttendanceResponse create(AttendanceRequest request) {
        Attendance attendance = new Attendance();
        attendance.setUsuario(getUser(request.getUsuarioId()));
        attendance.setQrCode(getQrCode(request.getQrCodeId()));
        return toResponse(attendanceRepository.save(attendance));
    }

    @Override
    public void delete(Long id) {
        attendanceRepository.delete(getById(id));
    }

    private Attendance getById(Long id) {
        return attendanceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asistencia no encontrada con id " + id));
    }

    private User getUser(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));
    }

    private QrCode getQrCode(Long id) {
        return qrCodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Codigo QR no encontrado con id " + id));
    }

    private AttendanceResponse toResponse(Attendance attendance) {
        return new AttendanceResponse(
                attendance.getId(),
                attendance.getUsuario().getId(),
                attendance.getUsuario().getNombre(),
                attendance.getQrCode().getId(),
                attendance.getQrCode().getTipo().name(),
                attendance.getFechaHora()
        );
    }
}
