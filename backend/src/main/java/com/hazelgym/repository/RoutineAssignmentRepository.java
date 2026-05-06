package com.hazelgym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hazelgym.model.RoutineAssignment;

public interface RoutineAssignmentRepository extends JpaRepository<RoutineAssignment, Long> {
}
