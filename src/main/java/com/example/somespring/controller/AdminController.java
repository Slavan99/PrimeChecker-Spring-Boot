package com.example.somespring.controller;

import com.example.somespring.entity.Role;
import com.example.somespring.entity.User;
import com.example.somespring.service.UserService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AdminController {

    @Autowired
    private UserService userService;


    @GetMapping("/admin")
    public String admin(@AuthenticationPrincipal User currentUser, Model model) {
        if (currentUser != null) {
            model.addAttribute("username", currentUser.getName());
            model.addAttribute("isAdmin", currentUser.isAdmin());
            model.addAttribute("isAuth", true);
        }
        if (!currentUser.getAuthorities().contains(Role.ADMIN)) {
            return "redirect:/";
        }
        Iterable<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin";
    }

    @PostMapping("/admin")
    public String postUser(@AuthenticationPrincipal User currentUser, @RequestParam String user_name,
                           @RequestParam boolean active, @RequestParam boolean admin) {
        if (currentUser.getAuthorities().contains(Role.ADMIN)) {
            userService.handleUserActiveAndAdmin(user_name, active, admin);
        }
        return "redirect:/admin";
    }


}
