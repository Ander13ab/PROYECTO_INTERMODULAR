package com.hazelgym.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hazelgym.dto.request.RoutineRequest;
import com.hazelgym.dto.response.RoutineResponse;
import com.hazelgym.exception.ResourceNotFoundException;
import com.hazelgym.model.RoleName;
import com.hazelgym.model.Routine;
import com.hazelgym.model.User;
import com.hazelgym.repository.RoutineAssignmentRepository;
import com.hazelgym.repository.RoutineRepository;
import com.hazelgym.repository.UserRepository;
import com.hazelgym.security.AuthUserDetails;
import com.hazelgym.service.RoutineService;

@Service
@Transactional
public class RoutineServiceImpl implements RoutineService {

    private final RoutineRepository routineRepository;
    private final RoutineAssignmentRepository routineAssignmentRepository;
    private final UserRepository userRepository;

    public RoutineServiceImpl(
            RoutineRepository routineRepository,
            RoutineAssignmentRepository routineAssignmentRepository,
            UserRepository userRepository
    ) {
        this.routineRepository = routineRepository;
        this.routineAssignmentRepository = routineAssignmentRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoutineResponse> findAll() {
        AuthUserDetails userDetails = getAuthenticatedUser();
        User currentUser = userDetails.getUser();

        List<Routine> routines = currentUser.getRole().getName() == RoleName.CLIENT
                ? findAssignedRoutines(currentUser.getId())
                : routineRepository.findAll();

        return routines
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoutineResponse findById(Long id) {
        AuthUserDetails userDetails = getAuthenticatedUser();
        User currentUser = userDetails.getUser();
        Routine routine = getRoutineById(id);

        if (currentUser.getRole().getName() == RoleName.CLIENT && !isAssignedToClient(routine.getId(), currentUser.getId())) {
            throw new ResourceNotFoundException("Rutina no encontrada con id " + id);
        }

        return toResponse(routine);
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

    private AuthUserDetails getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof AuthUserDetails userDetails)) {
            throw new ResourceNotFoundException("No hay un usuario autenticado en el contexto actual");
        }
        return userDetails;
    }

    private List<Routine> findAssignedRoutines(Long clientId) {
        Set<Long> assignedRoutineIds = routineAssignmentRepository.findByClienteIdOrderByFechaAsignacionDesc(clientId)
                .stream()
                .map(assignment -> assignment.getRutina().getId())
                .collect(java.util.stream.Collectors.toCollection(java.util.LinkedHashSet::new));

        return assignedRoutineIds.stream()
                .map(this::getRoutineById)
                .toList();
    }

    private boolean isAssignedToClient(Long routineId, Long clientId) {
        return routineAssignmentRepository.findByClienteIdOrderByFechaAsignacionDesc(clientId)
                .stream()
                .anyMatch(assignment -> assignment.getRutina().getId().equals(routineId));
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
