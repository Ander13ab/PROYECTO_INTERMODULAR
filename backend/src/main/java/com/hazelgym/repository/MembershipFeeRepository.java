package com.hazelgym.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hazelgym.model.MembershipFee;

public interface MembershipFeeRepository extends JpaRepository<MembershipFee, Long> {
}
