package com.b2.e_commerce.repository;

import com.b2.e_commerce.entity.Role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByRole(String role);
}
