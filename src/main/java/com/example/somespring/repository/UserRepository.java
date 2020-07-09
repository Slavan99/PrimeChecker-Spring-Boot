package com.example.somespring.repository;

import com.example.somespring.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer>, UserRepositoryCustom {
    User findByName(String name);
}
