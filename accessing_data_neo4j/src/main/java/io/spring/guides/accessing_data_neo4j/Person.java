package io.spring.guides.accessing_data_neo4j;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

import java.util.HashSet;
import java.util.Set;

@NodeEntity
public class Person {
    public String name;

    @RelatedTo(type = "TEAMMATE", direction = Direction.BOTH)
    public
    @Fetch
    Set<Person> teammates;

    @GraphId
    Long id;

    public Person() {
    }

    public Person(String name) {
        this.name = name;
    }

    public void worksWith(Person person) {
        if (teammates == null) {
            teammates = new HashSet<>();
        }
        teammates.add(person);
    }

    @Override
    public String toString() {
        if (teammates != null) {
            return teammates.stream().collect(
                    () -> new StringBuilder(name + "'s teammates include\n"),
                    (sb, p) -> sb.append("\t- ").append(p.name).append("\n"),
                    StringBuilder::append
            ).toString();
        } else {
            return name + " has no teammates";
        }
    }

    public String getName() {
        return name;
    }
}
