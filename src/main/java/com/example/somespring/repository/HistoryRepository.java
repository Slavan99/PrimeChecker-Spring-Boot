package com.example.somespring.repository;

import com.example.somespring.entity.Algorithm;
import com.example.somespring.entity.History;
import com.example.somespring.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HistoryRepository extends CrudRepository<History, Integer> {
    Page<History> findByUser(User user, Pageable pageable);
    List<History> findByAlgorithm(Algorithm algorithm);
}
