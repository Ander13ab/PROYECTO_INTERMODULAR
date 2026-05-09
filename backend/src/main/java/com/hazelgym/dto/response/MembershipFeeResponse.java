package com.hazelgym.dto.response;

import java.math.BigDecimal;

public record MembershipFeeResponse(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio
) {
}
