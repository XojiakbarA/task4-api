package com.task4.api.service.impl;

import com.task4.api.entity.User;
import com.task4.api.exception.UserAlreadyExistException;
import com.task4.api.repository.UserRepository;
import com.task4.api.request.ListIDRequest;
import com.task4.api.request.LoginRequest;
import com.task4.api.request.RegisterRequest;
import com.task4.api.security.jwt.JwtTokenProvider;
import com.task4.api.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
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
    public Map<Object, Object> login(LoginRequest request) {
        Map<Object, Object> res = new HashMap<>();
        String email = request.getEmail();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));
        User user = findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User with email: " + email + " not found");
        }
        String token = jwtTokenProvider.createToken(email);
        user.setLoggedInAt(new Date());
        userRepository.save(user);
        res.put("email", email);
        res.put("token", token);
        return res;
    }

    @Override
    public void store(RegisterRequest request) {
        String email = request.getEmail();
        if (userRepository.findByEmail(email) != null) {
            throw new UserAlreadyExistException("The email " + email + " has already been taken.");
        }
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
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
