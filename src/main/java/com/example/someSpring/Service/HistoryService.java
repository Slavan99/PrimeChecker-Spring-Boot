package com.example.someSpring.Service;

import com.example.someSpring.Entity.Algorithm;
import com.example.someSpring.Entity.History;
import com.example.someSpring.Entity.User;
import com.example.someSpring.PrimeChecker.fermat.FermatHandler;
import com.example.someSpring.PrimeChecker.millerrabin.MillerRabinHandler;
import com.example.someSpring.PrimeChecker.solovaystrassen.SolovayStrassenHandler;
import com.example.someSpring.PrimeChecker.trialdivision.TrialDivisionHandler;
import com.example.someSpring.Repository.AlgorithmRepository;
import com.example.someSpring.Repository.HistoryRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class HistoryService {
    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private AlgorithmService algorithmService;

    @Autowired
    private TrialDivisionHandler trialDivisionHandler;

    @Autowired
    private FermatHandler fermatHandler;

    @Autowired
    private MillerRabinHandler millerRabinHandler;

    @Autowired
    private SolovayStrassenHandler solovayStrassenHandler;

    @Autowired
    private Logger logger;

    public List<History> findByUser(User user) {
        return historyRepository.findByUser(user);
    }

    public String checkNumber(User currentUser, String algorithmName, String numberString, String iterString) throws ExecutionException, InterruptedException {
        History historyAdd = new History();
        historyAdd.setUser(currentUser);
        historyAdd.setAlgorithm(algorithmService.findByName(algorithmName));
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
