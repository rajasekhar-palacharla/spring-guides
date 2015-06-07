package io.spring.guides.accessing_data_mongodb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    @Autowired
    CustomerRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        repository.deleteAll();
        repository.save(new Customer("Alice", "Smith"));
        repository.save(new Customer("Bob", "Smith"));
        LOGGER.info("Customers found with findAll():");
        for (Customer customer : repository.findAll()) {
            LOGGER.info("{}", customer);
        }
        LOGGER.info("Customer found with findByFirstName('Alice'):");
        LOGGER.info("{}", repository.findByFirstName("Alice"));

        LOGGER.info("Customers found with findByLastName('Smith'):");
        for (Customer customer : repository.findByLastName("Smith")) {
            LOGGER.info("{}", customer);
        }
    }
}
