package io.spring.guides.accessing_data_jpa;

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
    CustomerRepository customerRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        customerRepository.save(new Customer("Jack", "Bauer"));
        customerRepository.save(new Customer("Chloe", "O'Brian"));
        customerRepository.save(new Customer("Kim", "Bauer"));
        customerRepository.save(new Customer("David", "Palmer"));
        customerRepository.save(new Customer("Michelle", "Dessler"));
        LOGGER.info("Customers found with findAll():");
        for (Customer customer : customerRepository.findAll()) {
            LOGGER.info("{}", customer);
        }
        LOGGER.info("Customer found with findOne(1l):");
        LOGGER.info("{}", customerRepository.findOne(1L));
        LOGGER.info("Customers found with findByLastName('Bauer'):");
        for (Customer customer : customerRepository.findByLastName("Bauer")) {
            LOGGER.info("{}", customer);
        }
    }
}
