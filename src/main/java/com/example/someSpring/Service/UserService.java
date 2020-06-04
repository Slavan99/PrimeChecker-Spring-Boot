package com.example.someSpring.Service;

import com.example.someSpring.Entity.Role;
import com.example.someSpring.Entity.User;
import com.example.someSpring.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public void handleUserActiveAndAdmin(Integer user_id, String active, String admin){
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
}
