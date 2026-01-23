package com.b2.e_commerce.service;

import com.b2.e_commerce.entity.User;
import com.b2.e_commerce.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User updateAccount(User user,
                              String name,
                              String firstname,
                              String mail,
                              String rawPassword) {

        user.setName(name);
        user.setFirstname(firstname);
        user.setMail(mail);

        if (rawPassword != null && !rawPassword.isBlank()) {
            user.setPassword(passwordEncoder.encode(rawPassword));
        }

        return userRepository.save(user);
    }
}
