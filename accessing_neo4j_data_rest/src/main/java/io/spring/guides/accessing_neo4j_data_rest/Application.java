package io.spring.guides.accessing_neo4j_data_rest;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;

@SpringBootApplication
@EnableNeo4jRepositories
public class Application extends Neo4jConfiguration {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    public Application() {
        setBasePackage("io.spring.guides.accessing_neo4j_data_rest");
    }

    @Bean(destroyMethod = "shutdown")
    GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory()
                .newEmbeddedDatabase("accessing_neo4j_data_rest/target/accessing_neo4j_data_rest.db");
    }
}
