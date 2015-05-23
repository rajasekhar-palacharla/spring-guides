package io.spring.guides.relational_data_access;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Application implements CommandLineRunner {
    @Autowired
    JdbcTemplate jdbcTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        System.out.println("Creating tables");
        jdbcTemplate.execute("drop table customers if exists");
        jdbcTemplate.execute("create table customers(id serial, first_name varchar(255), last_name varchar(255))");
        String[] names = {"John Woo", "Jeff Dean", "Josh Bloch", "Josh Long"};
        Arrays.stream(names).forEach(name -> {
            String[] nameParts = name.split(" ");
            System.out.printf("Inserting customer record for %s %s%n", nameParts[0], nameParts[1]);
            jdbcTemplate.update("insert into customers(first_name, last_name) values(?,?)", nameParts[0], nameParts[1]);
        });
        System.out.println("Querying for customer records where first_name = 'Josh'");
        List<Customer> results = jdbcTemplate.query(
                "select id, first_name, last_name from customers where first_name = ?",
                (rs, i) -> new Customer(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name")),
                "Josh"
        );
        results.stream().forEach(System.out::println);
    }
}
