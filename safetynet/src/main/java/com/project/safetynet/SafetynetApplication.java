package com.project.safetynet;

import com.project.safetynet.model.HelloWorld;
import com.project.safetynet.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.project.safetynet")
public class SafetynetApplication implements CommandLineRunner {

    @Autowired
    private BusinessService bs;


    public static void main(String[] args) {

        SpringApplication.run(SafetynetApplication.class, args);
    }

    public void run(String... args) throws Exception {
        HelloWorld hw = bs.getHelloWorld();
        System.out.println(hw);
    }
}
