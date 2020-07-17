package com.example.somespring.primechecker.trialdivision;

import com.example.somespring.primechecker.IPrimeChecker;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TrialDivisionHandler implements IPrimeChecker {

    private volatile boolean found = false;
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
            TrialDivisionThreadCallable threadCallable = new TrialDivisionThreadCallable(number, 3 + 2 * i);
            Future<Boolean> future = executorService.submit(threadCallable);
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
        return "Trial";
    }


    class TrialDivisionThreadCallable implements Callable<Boolean> {
        long numberToCheck;
        long startPos;

        public TrialDivisionThreadCallable(long numberToCheck, long startPos) {
            this.numberToCheck = numberToCheck;
            this.startPos = startPos;
        }


        @Override
        public Boolean call() {
            double sqrt = Math.sqrt(numberToCheck);
            for (long i = startPos; i <= sqrt; i += 2 * threadCount) {
                if (!found) {
                    if (numberToCheck % i == 0) {
                        found = true;
                        return false;
                    }
                }
            }
            return true;
        }
    }
}
