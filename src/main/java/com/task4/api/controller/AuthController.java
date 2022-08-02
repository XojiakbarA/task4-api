package com.task4.api.controller;

import com.task4.api.entity.User;
import com.task4.api.exception.UserAlreadyExistException;
import com.task4.api.request.LoginRequest;
import com.task4.api.security.jwt.JwtTokenProvider;
import com.task4.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtTokenProvider jwtTokenProvider;

    private final UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
    }

    @GetMapping("/user")
    public Object me(Authentication authentication) {
        return authentication.getPrincipal();
    }

    @PostMapping("/login")
    public ResponseEntity<Map<Object, Object>> login(@RequestBody LoginRequest request) {
        Map<Object, Object> response = new HashMap<>();
        try {
            String email = request.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, request.getPassword()));
            User user = userService.findByEmail(email);
            if (user == null) {
                throw new UsernameNotFoundException("User with email: " + email + " not found");
            }
            String token = jwtTokenProvider.createToken(email);
            response.put("email", email);
            response.put("token", token);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            response.put("message", "Invalid email and/or password.");
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        } catch (LockedException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.LOCKED).body(response);
        }
    }

    @PostMapping("register")
    public ResponseEntity<Map<Object, Object>> register(@RequestBody @Valid User user) {
        Map<Object, Object> response = new HashMap<>();
        try {
            userService.store(user);
            response.put("message", "Registration completed successfully. Please, login.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserAlreadyExistException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(response);
        }
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Map<String, String>> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        response.put("errors", errors);
        return response;
    }
}
