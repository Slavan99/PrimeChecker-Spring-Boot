package com.example.someSpring.Controller;

import com.example.someSpring.Entity.Role;
import com.example.someSpring.Entity.User;
import com.example.someSpring.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/registration")
    public String registration(){
        return "registration";
    }

    @PostMapping(produces = {"application/xml; charset=UTF-8"}, path = "/registration")
    public String addUser(User user, Map<String, Object> model){
        User byName = userRepository.findByName(user.getName());
        if(byName != null){
            model.put("message", "User exists!");
            return "registration";
        }

        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        userRepository.save(user);
        return "redirect:/login";
    }
}
