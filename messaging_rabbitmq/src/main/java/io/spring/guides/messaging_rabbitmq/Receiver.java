package io.spring.guides.messaging_rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;

import java.util.concurrent.CountDownLatch;

public class Receiver implements MessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);
    private CountDownLatch latch;

    public Receiver(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onMessage(Message message) {
        LOGGER.info("Received <" + message + ">");
        latch.countDown();
    }
}
