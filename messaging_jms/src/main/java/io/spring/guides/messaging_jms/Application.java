package io.spring.guides.messaging_jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.util.FileSystemUtils;

import java.io.File;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

    @Autowired
    JmsTemplate jmsTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
        FileSystemUtils.deleteRecursively(new File("activemq-data"));
        System.exit(0);
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Sending a new message...");
        jmsTemplate.send(Receiver.MAILBOX_DESTINATION, session -> session.createTextMessage("Hello, from JMS!"));
    }
}
