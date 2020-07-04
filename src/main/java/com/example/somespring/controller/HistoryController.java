
package com.example.somespring.controller;

import com.example.somespring.entity.Algorithm;
import com.example.somespring.entity.History;
import com.example.somespring.entity.User;
import com.example.somespring.service.AlgorithmService;
import com.example.somespring.service.HistoryService;
import com.example.somespring.service.PrimeNumberService;
import com.example.somespring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Controller
public class HistoryController {
    @Autowired
    private HistoryService historyService;

    @Autowired
    private AlgorithmService algorithmService;

    @Autowired
    private UserService userService;

    @Autowired
    private PrimeNumberService primeNumberService;


    @GetMapping("/history")
    public String history(@AuthenticationPrincipal User currentUser, Model model,
                          @PageableDefault(sort = {"checkDateTime"},
                                  direction = Sort.Direction.DESC) Pageable pageable) {
        if (currentUser != null) {
            model.addAttribute("username", currentUser.getName());
            model.addAttribute("isAdmin", currentUser.isAdmin());
            model.addAttribute("isAuth", true);
        }
        Page<History> historiesByUser = historyService.findByUser(currentUser, pageable);
        List<Algorithm> algorithms = algorithmService.findAll();
        model.addAttribute("algos", algorithms);
        if (historiesByUser.getTotalElements() == 0) {
            model.addAttribute("message", "You have no history!");
        } else {
            model.addAttribute("message", "");
            model.addAttribute("histories", historiesByUser);
            model.addAttribute("hashistory", true);
        }
        return "history";
    }

    @PostMapping("/history")
    public String addHistory(@AuthenticationPrincipal User currentUser, @ModelAttribute("name") String algorithmName,
                             @ModelAttribute("number") String numberString,
                             @ModelAttribute("iterations") String iterString) throws ExecutionException, InterruptedException {
        return primeNumberService.checkNumber(new History(), currentUser, algorithmName, numberString, iterString);
    }


}
