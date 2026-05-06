package com.hazelgym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hazelgym.model.ClassSession;

public interface ClassSessionRepository extends JpaRepository<ClassSession, Long> {
}
