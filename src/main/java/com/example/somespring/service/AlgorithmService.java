package com.example.somespring.service;

import com.example.somespring.entity.Algorithm;
import com.example.somespring.repository.AlgorithmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlgorithmService {
    @Autowired
    private AlgorithmRepository algorithmRepository;

    public Algorithm findByName(String name){
        return algorithmRepository.findByName(name);
    }

    public List<Algorithm> findAll(){
        return (List<Algorithm>) algorithmRepository.findAll();
    }
}
