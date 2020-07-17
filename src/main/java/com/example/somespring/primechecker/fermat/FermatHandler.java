package com.example.somespring.primechecker.fermat;

import com.example.somespring.primechecker.IPrimeChecker;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
@Scope("prototype")
public class FermatHandler implements IPrimeChecker {
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


    public static String getName() {
        return "Fermat";
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
