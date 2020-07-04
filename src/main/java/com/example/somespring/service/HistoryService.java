package com.example.somespring.service;

import com.example.somespring.entity.History;
import com.example.somespring.entity.User;
import com.example.somespring.primechecker.IPrimeChecker;
import com.example.somespring.primechecker.fermat.FermatHandler;
import com.example.somespring.primechecker.millerrabin.MillerRabinHandler;
import com.example.somespring.primechecker.solovaystrassen.SolovayStrassenHandler;
import com.example.somespring.primechecker.trialdivision.TrialDivisionHandler;
import com.example.somespring.repository.HistoryRepository;
import com.example.somespring.springconfiguration.annotations.SaveHistory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;


@Service
public class HistoryService {
    @Autowired
    private HistoryRepository historyRepository;

    public Page<History> findByUser(User user, Pageable pageable) {

        return historyRepository.findByUser(user, pageable);
    }


    public void save(History history) {
        historyRepository.save(history);
    }

}
