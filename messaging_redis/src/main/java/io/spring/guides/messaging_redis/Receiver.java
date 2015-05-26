package io.spring.guides.messaging_redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

import java.util.concurrent.CountDownLatch;

public class Receiver implements MessageListener {
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);
    private CountDownLatch latch;

    public Receiver(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        LOGGER.info("Received <" + message + ">");
        latch.countDown();
    }
}
