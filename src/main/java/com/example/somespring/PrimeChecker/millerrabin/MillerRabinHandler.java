
package com.example.somespring.primechecker.millerrabin;

import com.example.somespring.primechecker.IPrimeChecker;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

@Component
@Scope("prototype")
public class MillerRabinHandler implements IPrimeChecker {
    private static volatile boolean found = false;
    private static int threadCount = Runtime.getRuntime().availableProcessors();

    @Override
    public boolean isPrimeNumber(long number, int iter) throws ExecutionException, InterruptedException {
        found = false;
        if (number % 2 == 0) {
            return false;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<Future<Boolean>> futures = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            MillerRabinCheckerThread millerRabinCheckerThread = new MillerRabinCheckerThread(number, (int) Math.ceil(iter / threadCount));
            Future<Boolean> future = executorService.submit(millerRabinCheckerThread);
            futures.add(future);
        }
        boolean result = true;
        for (Future future : futures) {
            result &= (Boolean) future.get();
        }
        executorService.shutdown();
        return result;
    }


    public static String getName() {
        return "Miller-Rabin";
    }


    static class MillerRabinCheckerThread implements Callable<Boolean> {
        long numberToCheck;
        int iterations;

        public MillerRabinCheckerThread(long numberToCheck, int iterations) {
            this.numberToCheck = numberToCheck;
            this.iterations = iterations;
        }

        private long modPow(long a, long b, long c) {
            long res = 1;
            for (int i = 0; i < b; i++) {
                res *= a;
                res %= c;
            }
            return res % c;
        }

        private long mulMod(long a, long b, long mod) {
            return BigInteger.valueOf(a).multiply(BigInteger.valueOf(b)).mod(BigInteger.valueOf(mod)).longValue();
        }

        @Override
        public Boolean call() {
            if (!found) {
                long s = numberToCheck - 1;
                while (s % 2 == 0)
                    s /= 2;

                Random rand = new Random();
                for (int i = 0; i < iterations; i++) {
                    long r = Math.abs(rand.nextLong());
                    long a = r % (numberToCheck - 1) + 1, temp = s;
                    long mod = modPow(a, temp, numberToCheck);
                    while (temp != numberToCheck - 1 && mod != 1 && mod != numberToCheck - 1) {
                        mod = mulMod(mod, mod, numberToCheck);
                        temp *= 2;
                    }
                    if (mod != numberToCheck - 1 && temp % 2 == 0) {
                        found = true;
                        return false;
                    }
                }
            }
            return true;
        }
    }

}
