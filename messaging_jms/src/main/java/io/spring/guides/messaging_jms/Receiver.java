package io.spring.guides.messaging_jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class Receiver {
    public static final String MAILBOX_DESTINATION = "mailbox-destination";
    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    @JmsListener(destination = MAILBOX_DESTINATION)
    public void receiveMessage(String message) {
        LOGGER.info("Received <{}>", message);
    }
}
