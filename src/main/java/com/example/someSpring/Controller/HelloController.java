package com.example.someSpring.Controller;

import com.example.someSpring.Entity.User;
import com.example.someSpring.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class HelloController {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/hello")
    public String hello(Map<String, Object> model) {
        Iterable<User> users = userRepository.findAll();
        model.put("users", users);
        return "hello";
    }
}
