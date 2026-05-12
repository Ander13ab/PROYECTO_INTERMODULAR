package com.hazelgym.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hazelgym.model.Attendance;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    List<Attendance> findAllByOrderByFechaHoraDesc();

    List<Attendance> findByUsuarioIdOrderByFechaHoraDesc(Long usuarioId);
}
