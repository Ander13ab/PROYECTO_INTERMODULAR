package com.hazelgym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hazelgym.model.QrCode;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {
}
