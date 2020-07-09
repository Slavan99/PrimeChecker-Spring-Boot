package com.example.somespring.repository;

import com.example.somespring.entity.User;

public interface UserRepositoryCustom {
    void refresh(User user);

    void persist(User user);
}
