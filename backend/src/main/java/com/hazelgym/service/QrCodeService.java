package com.hazelgym.service;

import java.util.List;

import com.hazelgym.dto.request.QrCodeRequest;
import com.hazelgym.dto.response.QrCodeResponse;

public interface QrCodeService {

    List<QrCodeResponse> findAll();

    QrCodeResponse findById(Long id);

    QrCodeResponse create(QrCodeRequest request);

    QrCodeResponse update(Long id, QrCodeRequest request);

    void delete(Long id);
}
