package com.example.somespring.controller;

import com.example.somespring.entity.User;
import com.example.somespring.exception.IncorrectDataException;
import com.example.somespring.exception.UserAlreadyExistAuthenticationException;
import com.example.somespring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

    private String existingUserHandle(Model model) {
        model.addAttribute("message", "User exists!");
        return "registration";
    }

    @PostMapping(produces = {"application/xml; charset=UTF-8"}, path = "/registration")
    public String addUser(User user, Model model) throws Exception {
        try {
            userService.registerUser(user);
        } catch (DataIntegrityViolationException | UserAlreadyExistAuthenticationException e) {
            return existingUserHandle(model);
        } catch (IncorrectDataException e) {
            model.addAttribute("message", "Wrong data input!");
            return "registration";
        }
        return "redirect:/login";
    }
}
