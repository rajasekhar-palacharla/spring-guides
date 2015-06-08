package io.spring.guides.async_method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.Future;

@SpringBootApplication
@EnableAsync // Remove this annotation to see async benefit
public class Application implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    @Autowired
    FacebookLookupService facebookLookupService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        long start = System.currentTimeMillis();

        Future<Page> page1 = facebookLookupService.findPage("PivotalSoftware");
        Future<Page> page2 = facebookLookupService.findPage("CloudFoundry");
        Future<Page> page3 = facebookLookupService.findPage("SpringFramework");

        while (!page1.isDone() || !page2.isDone() || !page3.isDone()) {
            Thread.sleep(10);
        }

        LOGGER.info("Elapsed time: {} ms", System.currentTimeMillis() - start);
        LOGGER.info("{}", page1.get());
        LOGGER.info("{}", page2.get());
        LOGGER.info("{}", page3.get());
    }
}
