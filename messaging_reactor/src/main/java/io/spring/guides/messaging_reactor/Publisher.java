package io.spring.guides.messaging_reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class Publisher {
    private static final Logger LOGGER = LoggerFactory.getLogger(Publisher.class);

    @Autowired
    EventBus eventBus;
    @Autowired
    CountDownLatch latch;

    public void publishQuotes(int numberOfQuotes) throws InterruptedException {
        long start = System.currentTimeMillis();
        AtomicInteger counter = new AtomicInteger(1);
        for (int i = 0; i < numberOfQuotes; i++) {
            eventBus.notify("quotes", Event.wrap(counter.getAndIncrement()));
        }
        latch.await();
        long elapsed = System.currentTimeMillis() - start;
        LOGGER.info("Elapsed time: {}ms", elapsed);
        LOGGER.info("Average time per quote: {}ms", elapsed / numberOfQuotes);
    }
}
