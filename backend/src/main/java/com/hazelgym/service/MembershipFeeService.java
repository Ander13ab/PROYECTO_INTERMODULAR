package com.hazelgym.service;

import java.util.List;

import com.hazelgym.dto.request.MembershipFeeRequest;
import com.hazelgym.dto.response.MembershipFeeResponse;

public interface MembershipFeeService {

    List<MembershipFeeResponse> findAll();

    MembershipFeeResponse findById(Long id);

    MembershipFeeResponse create(MembershipFeeRequest request);

    MembershipFeeResponse update(Long id, MembershipFeeRequest request);

    void delete(Long id);
}
