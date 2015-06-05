package io.spring.guides.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class Application {
    public static void main(String[] args) throws Exception {
        SpringApplication.run("/blog/integration.xml", args);
    }
}
