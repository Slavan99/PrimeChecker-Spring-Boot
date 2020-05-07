package com.example.someSpring.Controller;

import com.example.someSpring.Entity.History;
import com.example.someSpring.Entity.User;
import com.example.someSpring.Repository.HistoryRepository;
import com.example.someSpring.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class HistoryController {
    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/history")
    public String history(Model model, RedirectAttributes redirectAttributes) {
        User user = (User) userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<History> historiesByUser = historyRepository.findByUser(user);
        if(historiesByUser.size() == 0){
            redirectAttributes.addFlashAttribute("message", "You have no number checks!");
            return "redirect:/greeting";
        }
        else {
            model.addAttribute("histories", historiesByUser);
            return "history";
        }
    }
}
