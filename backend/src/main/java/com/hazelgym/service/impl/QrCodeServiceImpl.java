package com.hazelgym.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hazelgym.dto.request.QrCodeRequest;
import com.hazelgym.dto.response.QrCodeResponse;
import com.hazelgym.exception.BadRequestException;
import com.hazelgym.exception.ResourceNotFoundException;
import com.hazelgym.model.ClassSession;
import com.hazelgym.model.Machine;
import com.hazelgym.model.QrCode;
import com.hazelgym.model.QrType;
import com.hazelgym.repository.ClassSessionRepository;
import com.hazelgym.repository.MachineRepository;
import com.hazelgym.repository.QrCodeRepository;
import com.hazelgym.service.QrCodeService;

@Service
@Transactional
public class QrCodeServiceImpl implements QrCodeService {

    private final QrCodeRepository qrCodeRepository;
    private final MachineRepository machineRepository;
    private final ClassSessionRepository classSessionRepository;

    public QrCodeServiceImpl(
            QrCodeRepository qrCodeRepository,
            MachineRepository machineRepository,
            ClassSessionRepository classSessionRepository
    ) {
        this.qrCodeRepository = qrCodeRepository;
        this.machineRepository = machineRepository;
        this.classSessionRepository = classSessionRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<QrCodeResponse> findAll() {
        return qrCodeRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public QrCodeResponse findById(Long id) {
        return toResponse(getById(id));
    }

    @Override
    public QrCodeResponse create(QrCodeRequest request) {
        QrCode qrCode = new QrCode();
        applyRequest(qrCode, request);
        return toResponse(qrCodeRepository.save(qrCode));
    }

    @Override
    public QrCodeResponse update(Long id, QrCodeRequest request) {
        QrCode qrCode = getById(id);
        applyRequest(qrCode, request);
        return toResponse(qrCodeRepository.save(qrCode));
    }

    @Override
    public void delete(Long id) {
        qrCodeRepository.delete(getById(id));
    }

    private QrCode getById(Long id) {
        return qrCodeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Codigo QR no encontrado con id " + id));
    }

    private Machine getMachine(Long id) {
        return machineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maquina no encontrada con id " + id));
    }

    private ClassSession getClassSession(Long id) {
        return classSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sesion de clase no encontrada con id " + id));
    }

    private void applyRequest(QrCode qrCode, QrCodeRequest request) {
        validateTypeRules(request);
        qrCode.setTipo(request.getTipo());
        qrCode.setEsEntradaGimnasio(request.getEsEntradaGimnasio());
        qrCode.setMaquina(request.getMaquinaId() != null ? getMachine(request.getMaquinaId()) : null);
        qrCode.setSesionClase(request.getSesionClaseId() != null ? getClassSession(request.getSesionClaseId()) : null);
    }

    private void validateTypeRules(QrCodeRequest request) {
        if (request.getTipo() == QrType.ENTRY) {
            if (!Boolean.TRUE.equals(request.getEsEntradaGimnasio()) || request.getMaquinaId() != null || request.getSesionClaseId() != null) {
                throw new BadRequestException("Un QR de tipo ENTRY debe ser de entrada y no puede vincularse a maquina ni sesion");
            }
        }

        if (request.getTipo() == QrType.MACHINE) {
            if (request.getMaquinaId() == null || request.getSesionClaseId() != null || Boolean.TRUE.equals(request.getEsEntradaGimnasio())) {
                throw new BadRequestException("Un QR de tipo MACHINE debe vincularse solo a una maquina");
            }
        }

        if (request.getTipo() == QrType.CLASS_SESSION) {
            if (request.getSesionClaseId() == null || request.getMaquinaId() != null || Boolean.TRUE.equals(request.getEsEntradaGimnasio())) {
                throw new BadRequestException("Un QR de tipo CLASS_SESSION debe vincularse solo a una sesion");
            }
        }
    }

    private QrCodeResponse toResponse(QrCode qrCode) {
        String sessionSummary = null;
        if (qrCode.getSesionClase() != null) {
            sessionSummary = qrCode.getSesionClase().getGymClass().getNombre() + " - " + qrCode.getSesionClase().getFecha();
        }

        return new QrCodeResponse(
                qrCode.getId(),
                qrCode.getTipo(),
                qrCode.getEsEntradaGimnasio(),
                qrCode.getMaquina() != null ? qrCode.getMaquina().getId() : null,
                qrCode.getMaquina() != null ? qrCode.getMaquina().getNombre() : null,
                qrCode.getSesionClase() != null ? qrCode.getSesionClase().getId() : null,
                sessionSummary
        );
    }
}
