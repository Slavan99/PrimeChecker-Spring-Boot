package com.example.somespring.service;

import com.example.somespring.entity.Role;
import com.example.somespring.entity.User;
import com.example.somespring.exception.IncorrectDataException;
import com.example.somespring.exception.UserAlreadyExistAuthenticationException;
import com.example.somespring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public void handleUserActiveAndAdmin(String user_name, boolean active, boolean admin) {
        User user = userRepository.findByName(user_name);

        if (active) {
            user.setActive(true);
        } else {
            user.setActive(false);
        }
        if (admin) {
            user.addRole(Role.ADMIN);
        } else {
            user.getAuthorities().remove(Role.ADMIN);
        }
        userRepository.save(user);
    }

    public void registerUser(User user) throws Exception {
        if (user.getName().equals("") || user.getPassword().equals("")) {
            throw new IncorrectDataException();
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


}
