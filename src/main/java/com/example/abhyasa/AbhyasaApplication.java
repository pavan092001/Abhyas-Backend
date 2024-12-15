package com.example.abhyasa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "com.example.abhyasa.repository")
public class AbhyasaApplication {

    public static void main(String[] args) {
        SpringApplication.run(AbhyasaApplication.class, args);
    }

}
