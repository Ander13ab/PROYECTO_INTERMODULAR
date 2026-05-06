package com.hazelgym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hazelgym.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
}
