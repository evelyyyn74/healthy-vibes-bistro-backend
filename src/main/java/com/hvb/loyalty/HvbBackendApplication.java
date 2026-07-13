package com.hvb.loyalty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HvbBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(HvbBackendApplication.class, args);
    }

}
