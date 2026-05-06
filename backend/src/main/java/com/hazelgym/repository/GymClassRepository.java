package com.hazelgym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hazelgym.model.GymClass;

public interface GymClassRepository extends JpaRepository<GymClass, Long> {
}
