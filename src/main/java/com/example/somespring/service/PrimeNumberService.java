package com.example.somespring.service;

import com.example.somespring.entity.History;
import com.example.somespring.entity.User;
import com.example.somespring.primechecker.IPrimeChecker;
import com.example.somespring.springconfiguration.annotations.SaveHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;


@Service
public class PrimeNumberService {

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
        IPrimeChecker primeChecker = handlerResolver.getCheckerByName(algorithmName);
        boolean result = primeChecker.isPrimeNumber(number, iterations);
        historyAdd.setResult(result);
        historyAdd.setNumber(number);
        historyAdd.setIterations(iterations);
        historyAdd.setCheckDateTime(LocalDateTime.now());
        return "redirect:/history";
    }
}
