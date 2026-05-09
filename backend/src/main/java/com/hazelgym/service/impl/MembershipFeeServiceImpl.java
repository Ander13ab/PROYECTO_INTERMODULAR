package com.hazelgym.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hazelgym.dto.request.MembershipFeeRequest;
import com.hazelgym.dto.response.MembershipFeeResponse;
import com.hazelgym.exception.ResourceNotFoundException;
import com.hazelgym.model.MembershipFee;
import com.hazelgym.repository.MembershipFeeRepository;
import com.hazelgym.service.MembershipFeeService;

@Service
@Transactional
public class MembershipFeeServiceImpl implements MembershipFeeService {

    private final MembershipFeeRepository membershipFeeRepository;

    public MembershipFeeServiceImpl(MembershipFeeRepository membershipFeeRepository) {
        this.membershipFeeRepository = membershipFeeRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MembershipFeeResponse> findAll() {
        return membershipFeeRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MembershipFeeResponse findById(Long id) {
        return toResponse(getById(id));
    }

    @Override
    public MembershipFeeResponse create(MembershipFeeRequest request) {
        MembershipFee fee = new MembershipFee();
        applyRequest(fee, request);
        return toResponse(membershipFeeRepository.save(fee));
    }

    @Override
    public MembershipFeeResponse update(Long id, MembershipFeeRequest request) {
        MembershipFee fee = getById(id);
        applyRequest(fee, request);
        return toResponse(membershipFeeRepository.save(fee));
    }

    @Override
    public void delete(Long id) {
        membershipFeeRepository.delete(getById(id));
    }

    private MembershipFee getById(Long id) {
        return membershipFeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cuota no encontrada con id " + id));
    }

    private void applyRequest(MembershipFee fee, MembershipFeeRequest request) {
        fee.setNombre(request.getNombre());
        fee.setDescripcion(request.getDescripcion());
        fee.setPrecio(request.getPrecio());
    }

    private MembershipFeeResponse toResponse(MembershipFee fee) {
        return new MembershipFeeResponse(
                fee.getId(),
                fee.getNombre(),
                fee.getDescripcion(),
                fee.getPrecio()
        );
    }
}
