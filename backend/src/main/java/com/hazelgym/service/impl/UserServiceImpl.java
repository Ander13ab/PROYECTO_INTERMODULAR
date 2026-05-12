package com.hazelgym.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hazelgym.dto.request.UserCreateRequest;
import com.hazelgym.dto.request.UserUpdateRequest;
import com.hazelgym.dto.response.UserResponse;
import com.hazelgym.exception.DuplicateResourceException;
import com.hazelgym.exception.ResourceNotFoundException;
import com.hazelgym.model.Role;
import com.hazelgym.model.RoleName;
import com.hazelgym.model.User;
import com.hazelgym.repository.RoleRepository;
import com.hazelgym.repository.UserRepository;
import com.hazelgym.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return toResponse(getUserById(id));
    }

    @Override
    public UserResponse create(UserCreateRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con el email indicado");
        }

        Role role = resolveRole(request.getRoleId(), request.getRoleName());

        User user = new User();
        user.setNombre(request.getNombre());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(role);
        user.setActivo(request.getActivo() != null ? request.getActivo() : Boolean.TRUE);

        return toResponse(userRepository.save(user));
    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = getUserById(id);

        if (!user.getEmail().equals(request.getEmail()) && userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con el email indicado");
        }

        user.setNombre(request.getNombre());
        user.setEmail(request.getEmail());
        user.setRole(resolveRole(request.getRoleId(), request.getRoleName()));
        user.setActivo(request.getActivo());

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return toResponse(userRepository.save(user));
    }

    @Override
    public void delete(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id " + id));
    }

    private Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id " + id));
    }

    private Role getRoleByName(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con nombre " + roleName));
    }

    private Role resolveRole(Long roleId, RoleName roleName) {
        if (roleId != null) {
            return getRoleById(roleId);
        }
        if (roleName != null) {
            return getRoleByName(roleName);
        }
        throw new ResourceNotFoundException("Debes indicar un roleId o un roleName valido");
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getNombre(),
                user.getEmail(),
                user.getRole().getName().name(),
                user.getActivo(),
                user.getFechaCreacion()
        );
    }
}
