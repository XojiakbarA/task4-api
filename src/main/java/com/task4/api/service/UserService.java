package com.task4.api.service;

import com.task4.api.entity.User;
import com.task4.api.request.ListIDRequest;
import com.task4.api.request.LoginRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Map;
import java.util.Optional;

public interface UserService {

    Page<User> getAll(PageRequest pageRequest);

    User findByEmail(String email);

    Optional<User> findById(Long id);

    void store(User user);

    Map<Object, Object> login(LoginRequest request);
    void lock(ListIDRequest request);

    void unlock(ListIDRequest request);

    void destroyIn(ListIDRequest request);

}
