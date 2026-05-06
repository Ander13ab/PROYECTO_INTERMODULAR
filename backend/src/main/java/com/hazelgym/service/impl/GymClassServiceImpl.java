package com.hazelgym.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hazelgym.dto.request.GymClassRequest;
import com.hazelgym.dto.response.GymClassResponse;
import com.hazelgym.exception.ResourceNotFoundException;
import com.hazelgym.model.GymClass;
import com.hazelgym.model.RoleName;
import com.hazelgym.model.User;
import com.hazelgym.repository.GymClassRepository;
import com.hazelgym.repository.UserRepository;
import com.hazelgym.service.GymClassService;

@Service
@Transactional
public class GymClassServiceImpl implements GymClassService {

    private final GymClassRepository gymClassRepository;
    private final UserRepository userRepository;

    public GymClassServiceImpl(GymClassRepository gymClassRepository, UserRepository userRepository) {
        this.gymClassRepository = gymClassRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<GymClassResponse> findAll() {
        return gymClassRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public GymClassResponse findById(Long id) {
        return toResponse(getGymClassById(id));
    }

    @Override
    public GymClassResponse create(GymClassRequest request) {
        GymClass gymClass = new GymClass();
        applyRequest(gymClass, request);
        return toResponse(gymClassRepository.save(gymClass));
    }

    @Override
    public GymClassResponse update(Long id, GymClassRequest request) {
        GymClass gymClass = getGymClassById(id);
        applyRequest(gymClass, request);
        return toResponse(gymClassRepository.save(gymClass));
    }

    @Override
    public void delete(Long id) {
        gymClassRepository.delete(getGymClassById(id));
    }

    private GymClass getGymClassById(Long id) {
        return gymClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Clase no encontrada con id " + id));
    }

    private User getTrainerById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Entrenador no encontrado con id " + id));

        if (user.getRole().getName() != RoleName.TRAINER) {
            throw new ResourceNotFoundException("El usuario con id " + id + " no tiene rol TRAINER");
        }

        return user;
    }

    private void applyRequest(GymClass gymClass, GymClassRequest request) {
        gymClass.setNombre(request.getNombre());
        gymClass.setDescripcion(request.getDescripcion());
        gymClass.setDuracion(request.getDuracion());
        gymClass.setEntrenador(getTrainerById(request.getEntrenadorId()));
        gymClass.setActiva(request.getActiva());
    }

    private GymClassResponse toResponse(GymClass gymClass) {
        return new GymClassResponse(
                gymClass.getId(),
                gymClass.getNombre(),
                gymClass.getDescripcion(),
                gymClass.getDuracion(),
                gymClass.getEntrenador().getId(),
                gymClass.getEntrenador().getNombre(),
                gymClass.getActiva()
        );
    }
}
