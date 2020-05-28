
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
import com.example.someSpring.Service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private HistoryRepository historyRepository;

    @Autowired
    private AlgorithmRepository algorithmRepository;

    @Autowired
    private UserService userService;


    @GetMapping("/history")
    public String history(Model model) {
        User user = (User) userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        List<History> historiesByUser = historyRepository.findByUser(user);
        List<Algorithm> algorithms = (List<Algorithm>) algorithmRepository.findAll();
        model.addAttribute("algos", algorithms);
        if (historiesByUser.size() == 0) {
            model.addAttribute("message", "You have no history!");
        } else {
            model.addAttribute("message", "");
            model.addAttribute("histories", historiesByUser);
        }
        return "history";
    }

    @PostMapping("/history")
    public String addHistory(@ModelAttribute("name") String algorithmName, @ModelAttribute("number") String numberString,
                             @ModelAttribute("iterations") String iterString, Model model) throws ExecutionException, InterruptedException {
        TrialDivisionHandler trialDivisionHandler = new TrialDivisionHandler();
        FermatHandler fermatHandler = new FermatHandler();
        MillerRabinHandler millerRabinHandler = new MillerRabinHandler();
        SolovayStrassenHandler solovayStrassenHandler = new SolovayStrassenHandler();
        model.addAttribute("message", "");
        User user = (User) userService.loadUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        History historyAdd = new History();
        historyAdd.setUser(user);
        historyAdd.setAlgorithm(algorithmRepository.findByName(algorithmName));
        long number;
        int iterations;
        try {
            number = Long.parseLong(numberString);
            iterations = Integer.parseInt(iterString);
        } catch (Exception e) {
            return "redirect:/history";
        }

        if ("Trial".equals(algorithmName)) {
            boolean result = trialDivisionHandler.isPrimeNumber(number, iterations);
            historyAdd.setResult(result);
        } else if ("Fermat".equals(algorithmName)) {
            boolean result = fermatHandler.isPrimeNumber(number, iterations);
            historyAdd.setResult(result);
        } else if ("Miller-Rabin".equals(algorithmName)) {
            boolean result = millerRabinHandler.isPrimeNumber(number, iterations);
            historyAdd.setResult(result);
        } else if ("Solovay-Strassen".equals(algorithmName)) {
            boolean result = solovayStrassenHandler.isPrimeNumber(number, iterations);
            historyAdd.setResult(result);
        }
        historyAdd.setNumber(number);
        historyAdd.setIterations(iterations);
        historyRepository.save(historyAdd);
        return "redirect:/history";
    }
}
