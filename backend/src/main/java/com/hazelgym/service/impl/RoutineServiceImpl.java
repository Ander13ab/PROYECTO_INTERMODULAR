package com.hazelgym.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hazelgym.dto.request.RoutineRequest;
import com.hazelgym.dto.response.RoutineResponse;
import com.hazelgym.exception.ResourceNotFoundException;
import com.hazelgym.model.RoleName;
import com.hazelgym.model.Routine;
import com.hazelgym.model.User;
import com.hazelgym.repository.RoutineRepository;
import com.hazelgym.repository.UserRepository;
import com.hazelgym.service.RoutineService;

@Service
@Transactional
public class RoutineServiceImpl implements RoutineService {

    private final RoutineRepository routineRepository;
    private final UserRepository userRepository;

    public RoutineServiceImpl(RoutineRepository routineRepository, UserRepository userRepository) {
        this.routineRepository = routineRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoutineResponse> findAll() {
        return routineRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoutineResponse findById(Long id) {
        return toResponse(getRoutineById(id));
    }

    @Override
    public RoutineResponse create(RoutineRequest request) {
        Routine routine = new Routine();
        applyRequest(routine, request);
        return toResponse(routineRepository.save(routine));
    }

    @Override
    public RoutineResponse update(Long id, RoutineRequest request) {
        Routine routine = getRoutineById(id);
        applyRequest(routine, request);
        return toResponse(routineRepository.save(routine));
    }

    @Override
    public void delete(Long id) {
        routineRepository.delete(getRoutineById(id));
    }

    private Routine getRoutineById(Long id) {
        return routineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rutina no encontrada con id " + id));
    }

    private User getTrainerById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrenador no encontrado con id " + id));

        if (user.getRole().getName() != RoleName.TRAINER) {
            throw new ResourceNotFoundException("El usuario con id " + id + " no tiene rol TRAINER");
        }

        return user;
    }

    private void applyRequest(Routine routine, RoutineRequest request) {
        routine.setNombre(request.getNombre());
        routine.setDescripcion(request.getDescripcion());
        routine.setEntrenador(getTrainerById(request.getEntrenadorId()));
    }

    private RoutineResponse toResponse(Routine routine) {
        return new RoutineResponse(
                routine.getId(),
                routine.getNombre(),
                routine.getDescripcion(),
                routine.getEntrenador().getId(),
                routine.getEntrenador().getNombre(),
                routine.getFechaCreacion()
        );
    }
}
