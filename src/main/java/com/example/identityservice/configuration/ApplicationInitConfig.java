package com.example.identityservice.configuration;


import com.example.identityservice.entity.Users;
import com.example.identityservice.enums.Roles;
import com.example.identityservice.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

@Slf4j
@Configuration
public class ApplicationInitConfig {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if (userRepository.findByuserName("admin").isEmpty() || userRepository.findByuserName("admin") == null) {
                var role = new HashSet<String>();
                role.add(Roles.ADMIN.name());
                Users users = new Users();
                users.setUserName("admin");
                users.setPassword(passwordEncoder.encode("admmin"));
//                users.setRole(role);
                userRepository.save(users);
                log.warn("admin user has been created with defaul password admin");
            }
        };
    }

}
