package com.example.somespring.service;

import com.example.somespring.entity.Role;
import com.example.somespring.entity.User;
import com.example.somespring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void handleUserActiveAndAdmin(Integer user_id, String active, String admin) {
        Optional<User> userById = userRepository.findById(user_id);
        if (userById.isPresent()) {
            User user = userById.get();
            if ("true".equals(active)) {
                user.setActive(true);
            } else {
                user.setActive(false);
            }
            if ("true".equals(admin)) {
                user.addRole(Role.ADMIN);
            } else {
                user.getAuthorities().remove(Role.ADMIN);
            }
            userRepository.save(user);
        }
    }

    public void registerUser(User user) throws Exception {
        if (user.getName().equals("") || user.getPassword().equals("")) {
            throw new Exception();
        }
        User byName = userRepository.findByName(user.getName());
        if (byName != null) {
            throw new UserAlreadyExistAuthenticationException("User exists!");
        }
        user.setActive(true);
        user.addRole(Role.USER);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.persist(user);
    }

    public static class UserAlreadyExistAuthenticationException extends AuthenticationException {

        public UserAlreadyExistAuthenticationException(final String msg) {
            super(msg);
        }

    }
}
