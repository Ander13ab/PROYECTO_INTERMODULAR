package com.hazelgym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hazelgym.model.Machine;

public interface MachineRepository extends JpaRepository<Machine, Long> {
}
