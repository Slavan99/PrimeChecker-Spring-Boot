
package com.example.someSpring.PrimeChecker.solovaystrassen;

import com.example.someSpring.PrimeChecker.IPrimeChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class SolovayStrassenHandler implements IPrimeChecker {
    private static volatile boolean found = false;
    private static int threadCount = Runtime.getRuntime().availableProcessors();

    public void handle(Long number, int iter) throws ExecutionException, InterruptedException {
        if (number < 3) {
            System.out.println("Число должно быть не меньше 3!");
            System.exit(0);
        }
        boolean result = isPrimeNumber(number, iter);
        if (result) {
            long phi = phi(number);
            double eps = phi / (2.0 * number);
            double prob = eps * iter;
            if(prob >= 100){
                prob = 100;
            }
            System.out.println("Число " + number + " простое с вероятностью " + Math.round(prob) + " %");
        } else {
            System.out.println("Число " + number + " составное");
        }

    }

    @Override
    public boolean isPrimeNumber(long number, int iter) throws ExecutionException, InterruptedException {
        found = false;
        if (number % 2 == 0) {
            return false;
        }
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        List<Future<Boolean>> futures = new ArrayList<>();
        for (int i = 0; i < threadCount; i++) {
            SolovayStrassenCheckerThread solovayStrassenCheckerThread = new SolovayStrassenCheckerThread(number, (int) Math.ceil(iter / threadCount));
            Future<Boolean> future = executorService.submit(solovayStrassenCheckerThread);
            futures.add(future);
        }
        boolean result = true;
        for (Future future : futures) {
            result &= (Boolean) future.get();
        }
        executorService.shutdown();
        return result;
    }

    static class SolovayStrassenCheckerThread implements Callable<Boolean> {
        long numberToCheck;
        int iterations;

        public SolovayStrassenCheckerThread(long numberToCheck, int iterations) {
            this.numberToCheck = numberToCheck;
            this.iterations = iterations;
        }

        public long Jacobi(long a, long b) {
            if (b <= 0 || b % 2 == 0)
                return 0;
            long j = 1L;
            if (a < 0) {
                a = -a;
                if (b % 4 == 3)
                    j = -j;
            }
            while (a != 0) {
                while (a % 2 == 0) {
                    a /= 2;
                    if (b % 8 == 3 || b % 8 == 5)
                        j = -j;
                }

                long temp = a;
                a = b;
                b = temp;

                if (a % 4 == 3 && b % 4 == 3)
                    j = -j;
                a %= b;
            }
            if (b == 1)
                return j;
            return 0;
        }

        public long modPow(long a, long b, long c) {
            long res = 1;
            for (int i = 0; i < b; i++) {
                res *= a;
                res %= c;
            }
            return res % c;
        }

        @Override
        public Boolean call() {
            if (!found) {
                Random rand = new Random();
                for (int i = 0; i < iterations; i++) {
                    long r = Math.abs(rand.nextLong());
                    long a = r % (numberToCheck - 1) + 1;
                    long jacobian = (numberToCheck + Jacobi(a, numberToCheck)) % numberToCheck;
                    long mod = modPow(a, (numberToCheck - 1) / 2, numberToCheck);
                    if (jacobian == 0 || mod != jacobian) {
                        found = true;
                        return false;
                    }
                }
            }
            return true;
        }
    }
}