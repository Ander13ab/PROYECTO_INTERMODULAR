package com.hazelgym.dto.request;

import jakarta.validation.constraints.NotNull;

public class RoutineAssignmentRequest {

    @NotNull
    private Long routineId;

    @NotNull
    private Long clientId;

    public Long getRoutineId() {
        return routineId;
    }

    public void setRoutineId(Long routineId) {
        this.routineId = routineId;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
