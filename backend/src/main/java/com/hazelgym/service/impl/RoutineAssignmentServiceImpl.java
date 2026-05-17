package com.hazelgym.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hazelgym.dto.request.RoutineAssignmentRequest;
import com.hazelgym.dto.response.RoutineAssignmentResponse;
import com.hazelgym.exception.ResourceNotFoundException;
import com.hazelgym.model.RoleName;
import com.hazelgym.model.Routine;
import com.hazelgym.model.RoutineAssignment;
import com.hazelgym.model.User;
import com.hazelgym.repository.RoutineAssignmentRepository;
import com.hazelgym.repository.RoutineRepository;
import com.hazelgym.repository.UserRepository;
import com.hazelgym.security.AuthUserDetails;
import com.hazelgym.service.RoutineAssignmentService;

@Service
@Transactional
public class RoutineAssignmentServiceImpl implements RoutineAssignmentService {

    private final RoutineAssignmentRepository routineAssignmentRepository;
    private final RoutineRepository routineRepository;
    private final UserRepository userRepository;

    public RoutineAssignmentServiceImpl(
            RoutineAssignmentRepository routineAssignmentRepository,
            RoutineRepository routineRepository,
            UserRepository userRepository
    ) {
        this.routineAssignmentRepository = routineAssignmentRepository;
        this.routineRepository = routineRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoutineAssignmentResponse> findAll() {
        AuthUserDetails userDetails = getAuthenticatedUser();
        User currentUser = userDetails.getUser();

        List<RoutineAssignment> assignments = currentUser.getRole().getName() == RoleName.CLIENT
                ? routineAssignmentRepository.findByClienteIdOrderByFechaAsignacionDesc(currentUser.getId())
                : routineAssignmentRepository.findAllByOrderByFechaAsignacionDesc();

        return assignments.stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RoutineAssignmentResponse findById(Long id) {
        AuthUserDetails userDetails = getAuthenticatedUser();
        User currentUser = userDetails.getUser();

        RoutineAssignment assignment = currentUser.getRole().getName() == RoleName.CLIENT
                ? getByIdForClient(id, currentUser.getId())
                : getById(id);

        return toResponse(assignment);
    }

    @Override
    public RoutineAssignmentResponse create(RoutineAssignmentRequest request) {
        RoutineAssignment assignment = new RoutineAssignment();
        assignment.setRutina(getRoutine(request.getRoutineId()));
        assignment.setCliente(getClient(request.getClientId()));
        return toResponse(routineAssignmentRepository.save(assignment));
    }

    @Override
    public void delete(Long id) {
        routineAssignmentRepository.delete(getById(id));
    }

    private RoutineAssignment getById(Long id) {
        return routineAssignmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Asignacion de rutina no encontrada con id " + id));
    }

    private RoutineAssignment getByIdForClient(Long id, Long clienteId) {
        return routineAssignmentRepository.findByIdAndClienteId(id, clienteId)
                .orElseThrow(() -> new ResourceNotFoundException("Asignacion de rutina no encontrada con id " + id));
    }

    private Routine getRoutine(Long id) {
        return routineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rutina no encontrada con id " + id));
    }

    private User getClient(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado con id " + id));
        if (user.getRole().getName() != RoleName.CLIENT) {
            throw new ResourceNotFoundException("El usuario con id " + id + " no tiene rol CLIENT");
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

    private RoutineAssignmentResponse toResponse(RoutineAssignment assignment) {
        return new RoutineAssignmentResponse(
                assignment.getId(),
                assignment.getRutina().getId(),
                assignment.getRutina().getNombre(),
                assignment.getCliente().getId(),
                assignment.getCliente().getNombre(),
                assignment.getFechaAsignacion()
        );
    }
}
