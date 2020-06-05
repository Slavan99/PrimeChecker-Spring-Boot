
package com.example.someSpring.Controller;

import com.example.someSpring.Entity.Algorithm;
import com.example.someSpring.Entity.History;
import com.example.someSpring.Entity.User;
import com.example.someSpring.PrimeChecker.fermat.FermatHandler;
import com.example.someSpring.PrimeChecker.millerrabin.MillerRabinHandler;
import com.example.someSpring.PrimeChecker.solovaystrassen.SolovayStrassenHandler;
import com.example.someSpring.PrimeChecker.trialdivision.TrialDivisionHandler;
import com.example.someSpring.Repository.AlgorithmRepository;
import com.example.someSpring.Repository.HistoryRepository;
import com.example.someSpring.Service.AlgorithmService;
import com.example.someSpring.Service.HistoryService;
import com.example.someSpring.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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



    @GetMapping("/history")
    public String history(@AuthenticationPrincipal User currentUser, Model model) {
        List<History> historiesByUser = historyService.findByUser(currentUser);
        List<Algorithm> algorithms = algorithmService.findAll();
        model.addAttribute("algos", algorithms);
        if (historiesByUser.size() == 0) {
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
                             @ModelAttribute("iterations") String iterString, Model model) throws ExecutionException, InterruptedException {
        return historyService.checkNumber(currentUser, algorithmName, numberString, iterString);
    }
}
