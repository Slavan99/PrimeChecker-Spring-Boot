package com.example.somespring.service;

import com.example.somespring.primechecker.IPrimeChecker;
import com.example.somespring.primechecker.fermat.FermatHandler;
import com.example.somespring.primechecker.millerrabin.MillerRabinHandler;
import com.example.somespring.primechecker.solovaystrassen.SolovayStrassenHandler;
import com.example.somespring.primechecker.trialdivision.TrialDivisionHandler;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class HandlerResolver {
    @FunctionalInterface
    private interface PrototypeBeanExtractor {
        IPrimeChecker getChecker(HandlerResolver service);
    }

    private final Map<String, PrototypeBeanExtractor> HANDLERS_MAP = new HashMap<>() {
        {
            put(TrialDivisionHandler.getName(), HandlerResolver::getTrialDivisionHandler);
            put(FermatHandler.getName(), HandlerResolver::getFermatHandler);
            put(MillerRabinHandler.getName(), HandlerResolver::getMillerRabinHandler);
            put(SolovayStrassenHandler.getName(), HandlerResolver::getSolovayStrassenHandler);
        }
    };

    public IPrimeChecker getCheckerByName(String name) {
        return HANDLERS_MAP.get(name).getChecker(this);
    }


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
}
