package com.hazelgym.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hazelgym.model.RoutineAssignment;

public interface RoutineAssignmentRepository extends JpaRepository<RoutineAssignment, Long> {

    List<RoutineAssignment> findAllByOrderByFechaAsignacionDesc();

    List<RoutineAssignment> findByClienteIdOrderByFechaAsignacionDesc(Long clienteId);

    Optional<RoutineAssignment> findByIdAndClienteId(Long id, Long clienteId);
}
