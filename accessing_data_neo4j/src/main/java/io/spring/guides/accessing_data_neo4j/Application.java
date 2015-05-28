package io.spring.guides.accessing_data_neo4j;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.core.GraphDatabase;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.StreamSupport;

@SpringBootApplication
@EnableNeo4jRepositories
public class Application extends Neo4jConfiguration implements CommandLineRunner {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    GraphDatabase graphDatabase;

    public Application() {
        setInitialEntitySet(Collections.singleton(Person.class));
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory().newEmbeddedDatabase("accessing_data_neo4j/target/accessing_data_neo4j.db");
    }

    @Override
    public void run(String... strings) throws Exception {
        Person greg = new Person("Greg");
        Person craig = new Person("Craig");
        Person roy = new Person("Roy");

        List<Person> people = Arrays.asList(greg, craig, roy);

        System.out.println("Before linking up with Neo4j...");
        people.stream().forEach(System.out::println);

        try (Transaction tx = graphDatabase.beginTx()) {
            personRepository.save(greg);
            personRepository.save(craig);
            personRepository.save(roy);

            greg = personRepository.findByName(greg.name);
            greg.worksWith(roy);
            greg.worksWith(craig);
            personRepository.save(greg);

            roy = personRepository.findByName(roy.name);
            roy.worksWith(craig);
            personRepository.save(roy);

            System.out.println("Lookup each person by name...");
            people.stream().map(Person::getName).map(personRepository::findByName).forEach(System.out::println);

            System.out.println("Lookup who works with Greg...");
            StreamSupport.stream(personRepository.findByTeammatesName("Greg").spliterator(), false)
                    .map(Person::getName).forEach(name -> System.out.println(name + " works with Greg"));
            tx.success();
        }
    }
}
