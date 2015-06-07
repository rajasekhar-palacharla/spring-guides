package io.spring.guides.accessing_data_mongodb;

import org.springframework.data.annotation.Id;


public class Customer {

    @Id
    private String id;

    private String firstName;
    private String lastName;

    public Customer() {
    }

    public Customer(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Customer{id='" + id + "', firstName='" + firstName + "', lastName='" + lastName + "'}";
    }
}
