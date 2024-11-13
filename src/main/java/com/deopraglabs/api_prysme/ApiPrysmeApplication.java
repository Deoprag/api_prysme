package com.deopraglabs.api_prysme;

import com.deopraglabs.api_prysme.security.SecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class ApiPrysmeApplication {

    public static void main(String[] args) { SpringApplication.run(ApiPrysmeApplication.class, args); }

}
