package com.hazelgym.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import com.hazelgym.model.User;
import com.hazelgym.model.RoleName;

public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "role")
    Optional<User> findByEmail(String email);

    @EntityGraph(attributePaths = "role")
    List<User> findAllByRole_Name(RoleName roleName);

    boolean existsByEmail(String email);
}
