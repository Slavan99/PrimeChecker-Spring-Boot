package com.example.someSpring.Repository;

import com.example.someSpring.Entity.Algorithm;
import org.springframework.data.repository.CrudRepository;

public interface AlgorithmRepository extends CrudRepository<Algorithm, Integer> {
    Algorithm findByName(String name);
}
