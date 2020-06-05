package com.example.someSpring.Controller;

import com.example.someSpring.Entity.User;
import com.example.someSpring.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.lang.String;

@Controller
public class GreetingController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String greeting(@AuthenticationPrincipal User user, Model model) {

        if (user != null) {
            model.addAttribute("name", user.getName());
            model.addAttribute("isAdmin", user.isAdmin());
            model.addAttribute("isAuth", true);
        } else {
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