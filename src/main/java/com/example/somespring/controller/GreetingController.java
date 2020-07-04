package com.example.somespring.controller;

import com.example.somespring.entity.User;
import com.example.somespring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.lang.String;

@Controller
public class GreetingController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String greeting(@AuthenticationPrincipal User user, Model model) {

        if (user != null) {
            model.addAttribute("name", user.getName());
            model.addAttribute("username", user.getName());
            model.addAttribute("isAdmin", user.isAdmin());
            model.addAttribute("isAuth", true);
        } else {
            model.addAttribute("username", "");
            model.addAttribute("name", "World");
        }
        return "greeting";
    }
    /*
    @PostMapping("filter")
    public String filter(@RequestParam String filter, Map<String, Object> model){
        Iterable<User> byName;
        if(filter != null && !filter.isEmpty()) {
            byName = userRepository.findByName(filter);
        }
        else{
            byName = userRepository.findAll();
        }
        model.put("users", byName);

        return "hello";
    }

     */

}