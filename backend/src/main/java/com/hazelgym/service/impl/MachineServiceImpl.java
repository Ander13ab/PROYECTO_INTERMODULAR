package com.hazelgym.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hazelgym.dto.request.MachineRequest;
import com.hazelgym.dto.response.MachineResponse;
import com.hazelgym.exception.ResourceNotFoundException;
import com.hazelgym.model.Machine;
import com.hazelgym.repository.MachineRepository;
import com.hazelgym.service.MachineService;

@Service
@Transactional
public class MachineServiceImpl implements MachineService {

    private final MachineRepository machineRepository;

    public MachineServiceImpl(MachineRepository machineRepository) {
        this.machineRepository = machineRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MachineResponse> findAll() {
        return machineRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public MachineResponse findById(Long id) {
        return toResponse(getMachineById(id));
    }

    @Override
    public MachineResponse create(MachineRequest request) {
        Machine machine = new Machine();
        applyRequest(machine, request);
        return toResponse(machineRepository.save(machine));
    }

    @Override
    public MachineResponse update(Long id, MachineRequest request) {
        Machine machine = getMachineById(id);
        applyRequest(machine, request);
        return toResponse(machineRepository.save(machine));
    }

    @Override
    public void delete(Long id) {
        Machine machine = getMachineById(id);
        machineRepository.delete(machine);
    }

    private Machine getMachineById(Long id) {
        return machineRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Maquina no encontrada con id " + id));
    }

    private void applyRequest(Machine machine, MachineRequest request) {
        machine.setNombre(request.getNombre());
        machine.setDescripcion(request.getDescripcion());
        machine.setGrupoMuscular(request.getGrupoMuscular());
        machine.setInstrucciones(request.getInstrucciones());
        machine.setNivel(request.getNivel());
        machine.setAdvertenciaSeguridad(request.getAdvertenciaSeguridad());
        machine.setImagenUrl(request.getImagenUrl());
        machine.setEstado(request.getEstado());
    }

    private MachineResponse toResponse(Machine machine) {
        return new MachineResponse(
                machine.getId(),
                machine.getNombre(),
                machine.getDescripcion(),
                machine.getGrupoMuscular(),
                machine.getInstrucciones(),
                machine.getNivel(),
                machine.getAdvertenciaSeguridad(),
                machine.getImagenUrl(),
                machine.getEstado()
        );
    }
}
