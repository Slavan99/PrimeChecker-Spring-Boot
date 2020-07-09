package com.example.somespring.repository;

import com.example.somespring.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public void refresh(User user) {
        User user1 = em.find(User.class, user.getName());
        em.refresh(user1);
    }

    @Transactional
    public void persist(User user) {
        em.persist(user);
    }
}
