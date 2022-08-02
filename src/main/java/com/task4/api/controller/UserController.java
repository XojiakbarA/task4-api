package com.task4.api.controller;

import com.task4.api.entity.User;
import com.task4.api.request.ListIDRequest;
import com.task4.api.service.UserService;
import com.task4.api.util.DefaultRequestParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<Page<User>> index(
            @RequestParam(value = "page", defaultValue = DefaultRequestParams.PAGE) int page,
            @RequestParam(value = "size", defaultValue = DefaultRequestParams.SIZE) int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> users = userService.getAll(pageRequest);
        return ResponseEntity.ok(users);
    }

    @PutMapping("/lock")
    public void lock(@RequestBody ListIDRequest request) {
        userService.lock(request);
    }

    @PutMapping("/unlock")
    public void unlock(@RequestBody ListIDRequest request) {
        userService.unlock(request);
    }

    @PostMapping("/destroy")
    public void destroy(@RequestBody ListIDRequest request) {
        userService.destroyIn(request);
    }
}
