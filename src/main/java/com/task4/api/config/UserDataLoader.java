package com.task4.api.config;

import com.github.javafaker.Faker;
import com.task4.api.entity.User;
import com.task4.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDataLoader  implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Faker faker;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        User initUser = new User();
        initUser.setFirstName("Xojiakbar");
        initUser.setLastName("Akramov");
        initUser.setEmail("xoji@mail.ru");
        initUser.setPassword(passwordEncoder.encode("123"));

        userRepository.save(initUser);

        for (int i = 0; i < 100; i++) {
            User user = new User();
            user.setFirstName(faker.name().firstName());
            user.setLastName(faker.name().lastName());
            user.setEmail(faker.internet().safeEmailAddress());
            user.setPassword(passwordEncoder.encode("321"));

            userRepository.save(user);
        }
    }
}
