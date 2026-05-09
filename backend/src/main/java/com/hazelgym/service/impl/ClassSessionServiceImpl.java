package com.hazelgym.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hazelgym.dto.request.ClassSessionRequest;
import com.hazelgym.dto.response.ClassSessionResponse;
import com.hazelgym.exception.BadRequestException;
import com.hazelgym.exception.ResourceNotFoundException;
import com.hazelgym.model.ClassSession;
import com.hazelgym.model.GymClass;
import com.hazelgym.repository.ClassSessionRepository;
import com.hazelgym.repository.GymClassRepository;
import com.hazelgym.service.ClassSessionService;

@Service
@Transactional
public class ClassSessionServiceImpl implements ClassSessionService {

    private final ClassSessionRepository classSessionRepository;
    private final GymClassRepository gymClassRepository;

    public ClassSessionServiceImpl(ClassSessionRepository classSessionRepository, GymClassRepository gymClassRepository) {
        this.classSessionRepository = classSessionRepository;
        this.gymClassRepository = gymClassRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ClassSessionResponse> findAll() {
        return classSessionRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ClassSessionResponse findById(Long id) {
        return toResponse(getById(id));
    }

    @Override
    public ClassSessionResponse create(ClassSessionRequest request) {
        validateTimes(request);
        ClassSession session = new ClassSession();
        applyRequest(session, request);
        return toResponse(classSessionRepository.save(session));
    }

    @Override
    public ClassSessionResponse update(Long id, ClassSessionRequest request) {
        validateTimes(request);
        ClassSession session = getById(id);
        applyRequest(session, request);
        return toResponse(classSessionRepository.save(session));
    }

    @Override
    public void delete(Long id) {
        classSessionRepository.delete(getById(id));
    }

    private ClassSession getById(Long id) {
        return classSessionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sesion de clase no encontrada con id " + id));
    }

    private GymClass getGymClass(Long id) {
        return gymClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clase no encontrada con id " + id));
    }

    private void validateTimes(ClassSessionRequest request) {
        if (!request.getHoraFin().isAfter(request.getHoraInicio())) {
            throw new BadRequestException("La hora de fin debe ser posterior a la hora de inicio");
        }
    }

    private void applyRequest(ClassSession session, ClassSessionRequest request) {
        session.setGymClass(getGymClass(request.getGymClassId()));
        session.setFecha(request.getFecha());
        session.setHoraInicio(request.getHoraInicio());
        session.setHoraFin(request.getHoraFin());
    }

    private ClassSessionResponse toResponse(ClassSession session) {
        return new ClassSessionResponse(
                session.getId(),
                session.getGymClass().getId(),
                session.getGymClass().getNombre(),
                session.getFecha(),
                session.getHoraInicio(),
                session.getHoraFin()
        );
    }
}
