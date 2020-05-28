package com.example.someSpring.PrimeChecker.fermat;

import com.example.someSpring.PrimeChecker.IPrimeChecker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class FermatHandler implements IPrimeChecker {
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
            double eps = phi / (1.0 * number);
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
            FermatCheckerThread fermatCheckerThread = new FermatCheckerThread(number, (int) Math.ceil(iter / threadCount));
            Future<Boolean> future = executorService.submit(fermatCheckerThread);
            futures.add(future);
        }
        boolean result = true;
        for (Future future : futures) {
            result &= (Boolean) future.get();
        }
        executorService.shutdown();
        return result;
    }

    static class FermatCheckerThread implements Callable<Boolean> {
        long numberToCheck;
        int iterations;

        public FermatCheckerThread(long numberToCheck, int iterations) {
            this.numberToCheck = numberToCheck;
            this.iterations = iterations;
        }

        private long binaryPower(long a, long n) {
            a %= n;
            long res = 1;
            long nMinusOne = n - 1;
            while (nMinusOne > 0) {
                if (nMinusOne % 2 == 1)
                    res = res * a % n;
                a = a * a % n;
                nMinusOne >>= 1;
            }
            return res;
        }

        @Override
        public Boolean call() {
            if (!found) {
                for (int i = 0; i < iterations; i++) {
                    int a = (int) (Math.random() * (numberToCheck - 3)) + 2;
                    if (binaryPower(a, numberToCheck) != 1) {
                        found = true;
                        return false;
                    }
                }
            }
            return true;
        }
    }

}