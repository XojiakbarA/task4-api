package com.task4.api.service;

import com.task4.api.entity.User;
import com.task4.api.request.ListIDRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;

public interface UserService {

    Page<User> getAll(PageRequest pageRequest);

    User findByEmail(String email);

    Optional<User> findById(Long id);

    User store(User user);

    void lock(ListIDRequest request);

    void unlock(ListIDRequest request);

    void destroyIn(ListIDRequest request);

}
