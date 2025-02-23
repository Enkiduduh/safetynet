package com.project.safetynet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = "com.project.safetynet")
@EnableJpaRepositories(basePackages = "com.project.safetynet.repository")
@EntityScan(basePackages = "com.project.safetynet.model")
public class SafetynetApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(SafetynetApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

    }
}
