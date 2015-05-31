package io.spring.guides.batch_processing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

public class PersonItemProcessor implements ItemProcessor<Person, Person> {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonItemProcessor.class);

    @Override
    public Person process(Person person) throws Exception {
        Person transformedPerson = new Person(person.getFirstName().toUpperCase(), person.getLastName().toUpperCase());
        LOGGER.info("Converting ({}) into ({})", person, transformedPerson);
        return transformedPerson;
    }
}
