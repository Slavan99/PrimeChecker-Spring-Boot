package com.example.someSpring.Repository;

import com.example.someSpring.Entity.Algorithm;
import com.example.someSpring.Entity.History;
import com.example.someSpring.Entity.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface HistoryRepository extends CrudRepository<History, Integer> {
    List<History> findByUser(User user);
    List<History> findByAlgorithm(Algorithm algorithm);
}
