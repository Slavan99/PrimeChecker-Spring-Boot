package com.example.somespring.service;

import com.example.somespring.entity.User;
import com.example.somespring.primechecker.IPrimeChecker;
import com.example.somespring.springconfiguration.annotations.SaveHistory;
import org.apache.commons.lang.mutable.MutableBoolean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;


@Service
public class PrimeNumberService {

    @Autowired
    private HandlerResolver handlerResolver;

    @SaveHistory
    public boolean checkNumber(User currentUser, String algorithmName, String numberString, String iterString) throws ExecutionException, InterruptedException {
        long number;
        int iterations;
        number = Long.parseLong(numberString);
        iterations = Integer.parseInt(iterString);
        IPrimeChecker primeChecker = handlerResolver.getCheckerByName(algorithmName);
        return primeChecker.isPrimeNumber(number, iterations);
    }
}
