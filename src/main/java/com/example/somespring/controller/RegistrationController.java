package com.example.somespring.controller;

import com.example.somespring.entity.User;
import com.example.somespring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("message", "");
        return "registration";
    }

    @PostMapping(produces = {"application/xml; charset=UTF-8"}, path = "/registration")
    public String addUser(User user, Model model) throws Exception {
        return userService.registerUser(user, model);
    }
}
