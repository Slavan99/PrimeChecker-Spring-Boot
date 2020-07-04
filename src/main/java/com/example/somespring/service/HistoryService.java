package com.example.someSpring.Service;

import com.example.someSpring.Entity.History;
import com.example.someSpring.Entity.User;
import com.example.someSpring.PrimeChecker.fermat.FermatHandler;
import com.example.someSpring.PrimeChecker.millerrabin.MillerRabinHandler;
import com.example.someSpring.PrimeChecker.solovaystrassen.SolovayStrassenHandler;
import com.example.someSpring.PrimeChecker.trialdivision.TrialDivisionHandler;
import com.example.someSpring.Repository.HistoryRepository;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class HistoryService {
    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private AlgorithmService algorithmService;


    @Lookup
    public SolovayStrassenHandler getSolovayStrassenHandler() {
        return null;
    }

    @Lookup
    public MillerRabinHandler getMillerRabinHandler() {
        return null;
    }

    @Lookup
    public FermatHandler getFermatHandler() {
        return null;
    }

    @Lookup
    public TrialDivisionHandler getTrialDivisionHandler() {
        return null;
    }

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
            TrialDivisionHandler trialDivisionHandler = getTrialDivisionHandler();
            boolean result = trialDivisionHandler.isPrimeNumber(number, iterations);
            historyAdd.setResult(result);
        } else if ("Fermat".equals(algorithmName)) {
            FermatHandler fermatHandler = getFermatHandler();
            boolean result = fermatHandler.isPrimeNumber(number, iterations);
            historyAdd.setResult(result);
        } else if ("Miller-Rabin".equals(algorithmName)) {
            MillerRabinHandler millerRabinHandler = getMillerRabinHandler();
            boolean result = millerRabinHandler.isPrimeNumber(number, iterations);
            historyAdd.setResult(result);
        } else if ("Solovay-Strassen".equals(algorithmName)) {
            SolovayStrassenHandler solovayStrassenHandler = getSolovayStrassenHandler();
            boolean result = solovayStrassenHandler.isPrimeNumber(number, iterations);
            historyAdd.setResult(result);
        }
        historyAdd.setNumber(number);
        historyAdd.setIterations(iterations);
        historyRepository.save(historyAdd);
        return "redirect:/history";
    }

}
