package io.spring.guides.messaging_reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import reactor.bus.Event;
import reactor.fn.Consumer;

import java.util.concurrent.CountDownLatch;

@Service
public class Receiver implements Consumer<Event<Integer>> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);
    @Autowired
    CountDownLatch latch;
    RestTemplate restTemplate = new RestTemplate();

    @Override
    public void accept(Event<Integer> integerEvent) {
        QuoteResource quoteResource = restTemplate.getForObject(
                "http://gturnquist-quoters.cfapps.io/api/random",
                QuoteResource.class
        );
        LOGGER.info("Quote {}: {}", integerEvent.getData(), quoteResource.getValue().getQuote());
        latch.countDown();
    }
}
