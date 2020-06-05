package com.example.someSpring.Controller;

import com.example.someSpring.Entity.Role;
import com.example.someSpring.Entity.User;
import com.example.someSpring.Repository.UserRepository;
import com.example.someSpring.Service.UserService;
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

    @Autowired
    private Logger logger;


    /*@GetMapping("/hello")
    public String hello(Model model) {
        User user = (User) userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!user.getAuthorities().contains(Role.ADMIN)) {
            return "redirect:/";
        }
        Iterable<User> users = userRepository.findAll();
        logger.info("Users put : " + users);
        model.addAttribute("users", users);
        model.addAttribute("message", "This is message");
        return "hello";
    }

    @PostMapping("/hello")
    public String postUser(@Valid User user, @ModelAttribute("active") String activeStr,
                           @ModelAttribute("admin") String adminStr, Model model) {
        try {
            logger.info("Model users : " + model.getAttribute("users"));
            logger.info("Model message : " + model.getAttribute("message"));
            logger.info("User : " + user);
            if ("true".equals(activeStr)) {
                user.setActive(true);
            } else if ("false".equals(activeStr)) {
                user.setActive(false);
            }
            if ("true".equals(adminStr)) {
                user.addRole(Role.ADMIN);
            } else {
                user.getAuthorities().remove(Role.ADMIN);
            }
            userRepository.save(user);
        } catch (NullPointerException npe) {
            logger.info(Arrays.toString(npe.getStackTrace()));
        }

        return "redirect:/hello";
    }*/

    @GetMapping("/admin")
    public String admin(@AuthenticationPrincipal User currentUser, Model model) {
        if (!currentUser.getAuthorities().contains(Role.ADMIN)) {
            return "redirect:/";
        }
        Iterable<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "admin";
    }

    @PostMapping("/admin")
    public String postUser(@AuthenticationPrincipal User currentUser, @RequestParam Integer user_id,
                           @RequestParam String active, @RequestParam String admin) {
        if (currentUser.getAuthorities().contains(Role.ADMIN)) {
            userService.handleUserActiveAndAdmin(user_id, active, admin);
        }
        return "redirect:/admin";
    }


}
