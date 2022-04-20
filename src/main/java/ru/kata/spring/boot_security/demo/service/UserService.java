package ru.kata.spring.boot_security.demo.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.kata.spring.boot_security.demo.model.User;

import java.util.List;

public interface UserService extends UserDetailsService {
    void saveUser(User user);

    void deleteById(Long id);

    User getById(Long id);

    User findByUsername(String username);

    List<User> findAll();

    UserDetails loadUserByUsername(String username);
}
