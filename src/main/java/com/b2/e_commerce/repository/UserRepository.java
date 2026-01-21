package com.b2.e_commerce.repository;

import com.b2.e_commerce.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByMail(String mail);
}
