package io.spring.guides.messaging_reactor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import reactor.Environment;
import reactor.bus.EventBus;

import java.util.concurrent.CountDownLatch;

import static reactor.bus.selector.Selectors.$;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final int NUMBER_OF_QUOTES = 10;
    @Autowired
    private EventBus eventBus;
    @Autowired
    private Receiver receiver;
    @Autowired
    private Publisher publisher;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    Environment env() {
        return Environment.initializeIfEmpty().assignErrorJournal();
    }

    @Bean
    EventBus createEventBus(Environment environment) {
        return EventBus.create(environment, Environment.THREAD_POOL);
    }

    @Bean
    public CountDownLatch countDownLatch() {
        return new CountDownLatch(NUMBER_OF_QUOTES);
    }

    @Override
    public void run(String... args) throws Exception {
        eventBus.on($("quotes"), receiver);
        publisher.publishQuotes(NUMBER_OF_QUOTES);
    }
}
