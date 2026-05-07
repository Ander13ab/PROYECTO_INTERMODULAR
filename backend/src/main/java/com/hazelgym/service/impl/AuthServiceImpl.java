package com.hazelgym.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hazelgym.dto.request.LoginRequest;
import com.hazelgym.dto.request.RegisterRequest;
import com.hazelgym.dto.response.AuthResponse;
import com.hazelgym.dto.response.AuthenticatedUserResponse;
import com.hazelgym.exception.DuplicateResourceException;
import com.hazelgym.exception.ResourceNotFoundException;
import com.hazelgym.model.Role;
import com.hazelgym.model.RoleName;
import com.hazelgym.model.User;
import com.hazelgym.repository.RoleRepository;
import com.hazelgym.repository.UserRepository;
import com.hazelgym.security.AuthUserDetails;
import com.hazelgym.security.JwtService;
import com.hazelgym.service.AuthService;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            AuthenticationManager authenticationManager
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Ya existe un usuario con el email indicado");
        }

        Role clientRole = roleRepository.findByName(RoleName.CLIENT)
                .orElseThrow(() -> new ResourceNotFoundException("No existe el rol CLIENT en la base de datos"));

        User user = new User();
        user.setNombre(request.getNombre());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(clientRole);
        user.setActivo(Boolean.TRUE);

        User savedUser = userRepository.save(user);
        AuthUserDetails userDetails = new AuthUserDetails(savedUser);
        String token = jwtService.generateToken(userDetails);

        return toAuthResponse(savedUser, token);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        AuthUserDetails userDetails = (AuthUserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);

        return toAuthResponse(userDetails.getUser(), token);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthenticatedUserResponse me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof AuthUserDetails userDetails)) {
            throw new ResourceNotFoundException("No hay un usuario autenticado en el contexto actual");
        }

        User user = userDetails.getUser();
        return new AuthenticatedUserResponse(
                user.getId(),
                user.getNombre(),
                user.getEmail(),
                user.getRole().getName().name(),
                user.getActivo()
        );
    }

    private AuthResponse toAuthResponse(User user, String token) {
        return new AuthResponse(
                token,
                "Bearer",
                user.getId(),
                user.getNombre(),
                user.getEmail(),
                user.getRole().getName().name()
        );
    }
}
