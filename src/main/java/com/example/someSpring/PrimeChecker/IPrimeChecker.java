package com.example.someSpring.PrimeChecker;

import java.util.concurrent.ExecutionException;

public interface IPrimeChecker {
    boolean isPrimeNumber(long n, int iter) throws ExecutionException, InterruptedException;

    default long phi(Long n) {
        long result = n;
        for (long i=2; i*i<=n; ++i)
            if (n % i == 0) {
                while (n % i == 0)
                    n /= i;
                result -= result / i;
            }
        if (n > 1)
            result -= result / n;
        return result;
    }
}
