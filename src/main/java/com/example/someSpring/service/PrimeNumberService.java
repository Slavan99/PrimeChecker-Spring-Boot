package com.example.somespring.service;

import com.example.somespring.entity.History;
import com.example.somespring.entity.User;
import com.example.somespring.primechecker.IPrimeChecker;
import com.example.somespring.springconfiguration.annotations.SaveHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;


@Service
public class PrimeNumberService {

    @Autowired
    private HistoryService historyService;

    @Autowired
    private HandlerResolver handlerResolver;

    @SaveHistory
    public String checkNumber(History historyAdd, User currentUser, String algorithmName, String numberString, String iterString) throws ExecutionException, InterruptedException {
        historyAdd.setUser(currentUser);
        long number;
        int iterations;
        try {
            number = Long.parseLong(numberString);
            iterations = Integer.parseInt(iterString);
        } catch (Exception e) {
            return "redirect:/history";
        }
        /*
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

         */

        IPrimeChecker primeChecker = handlerResolver.getCheckerByName(algorithmName);
        boolean result = primeChecker.isPrimeNumber(number, iterations);
        historyAdd.setResult(result);
        historyAdd.setNumber(number);
        historyAdd.setIterations(iterations);
        historyAdd.setCheckDateTime(LocalDateTime.now());
        historyService.save(historyAdd);
        return "redirect:/history";
    }
}
