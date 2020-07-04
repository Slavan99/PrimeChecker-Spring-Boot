package com.example.somespring.service;

import com.example.somespring.entity.Role;
import com.example.somespring.entity.User;
import com.example.somespring.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.StampedLock;

@Service
public class UserService implements UserDetailsService {

    public volatile static String currentName = "";
    public volatile static String prevName = "";
    public volatile static int count = 0;

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

    public String registerUser(User user, Model model) throws Exception {
        if (user.getName().equals("") || user.getPassword().equals("")) {
            model.addAttribute("message", "Wrong data input!");
            return "registration";
        }
        User byName = userRepository.findByName(user.getName());
        if (byName != null) {
            model.addAttribute("message", "User exists!");
            return "registration";
        }
        prevName = currentName;
        currentName = user.getName();
        if (count == 0) {
            count++;
            Thread.sleep(10000);
            user.setActive(true);
            user.addRole(Role.USER);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
            count = 0;
            return "redirect:/login";
        } else {
            if (prevName.equals(currentName)) {
                model.addAttribute("message", "User exists!");
                return "registration";
            } else {
                user.setActive(true);
                user.addRole(Role.USER);
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                userRepository.save(user);
                return "redirect:/login";
            }
        }
        ///
    }
}