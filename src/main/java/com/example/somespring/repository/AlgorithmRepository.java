package com.example.somespring.repository;

import com.example.somespring.entity.Algorithm;
import org.springframework.data.repository.CrudRepository;

public interface AlgorithmRepository extends CrudRepository<Algorithm, Integer> {
    Algorithm findByName(String name);
}
