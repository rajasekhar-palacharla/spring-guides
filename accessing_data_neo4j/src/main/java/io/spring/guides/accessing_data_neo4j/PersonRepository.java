package io.spring.guides.accessing_data_neo4j;

import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, String> {
    Person findByName(String name);

    Iterable<Person> findByTeammatesName(String name);
}
