package com.hazelgym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hazelgym.model.Routine;

public interface RoutineRepository extends JpaRepository<Routine, Long> {
}
