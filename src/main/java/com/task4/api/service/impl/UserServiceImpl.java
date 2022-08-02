package com.task4.api.service.impl;

import com.task4.api.entity.User;
import com.task4.api.exception.UserAlreadyExistException;
import com.task4.api.repository.UserRepository;
import com.task4.api.request.ListIDRequest;
import com.task4.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Page<User> getAll(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public User store(User user) {
        String email = user.getEmail();
        if (userRepository.findByEmail(email) != null) {
            throw new UserAlreadyExistException("The email " + email + " has already been taken.");
        }
        User newUser = new User();
        newUser.setFirstName(user.getFirstName());
        newUser.setLastName(user.getLastName());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(newUser);
    }

    @Override
    public void lock(ListIDRequest request) {
        for (Long id : request.getUserIDs()) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("User with id: " + id + " not found."));
            user.setNonLocked(false);
            //JWT
            userRepository.save(user);
        }
    }

    @Override
    public void unlock(ListIDRequest request) {
        for (Long id : request.getUserIDs()) {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new UsernameNotFoundException("User with id: " + id + " not found."));
            user.setNonLocked(true);
            userRepository.save(user);
        }
    }

    @Override
    public void destroyIn(ListIDRequest request) {
        for (Long id : request.getUserIDs()) {
            userRepository.deleteById(id);
            //JWT
        }
    }
}
