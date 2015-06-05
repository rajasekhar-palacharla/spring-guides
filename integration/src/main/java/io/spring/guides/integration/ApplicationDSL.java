package io.spring.guides.integration;

import com.sun.syndication.feed.synd.SyndEntry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.core.Pollers;
import org.springframework.integration.dsl.file.Files;
import org.springframework.integration.feed.inbound.FeedEntryMessageSource;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.scheduling.PollerMetadata;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.System.getProperty;

@SpringBootApplication()
public class ApplicationDSL {
    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationDSL.class, args);
    }

    @Bean(name = PollerMetadata.DEFAULT_POLLER)
    public PollerMetadata poller() {
        return Pollers.fixedRate(5000).get();
    }

    @Bean
    public IntegrationFlow flow() throws MalformedURLException {
        return IntegrationFlows
                .from(new FeedEntryMessageSource(new URL("https://spring.io/blog.atom"), "news"))
                .<SyndEntry, String>transform(e -> e.getTitle() + " @ " + e.getLink() + getProperty("line.separator"))
                .handle(Files
                        .outboundAdapter(new File("/tmp/si"))
                        .fileExistsMode(FileExistsMode.APPEND)
                        .charset("UTF-8")
                        .fileNameGenerator(message -> "SpringBoot.txt")
                        .get())
                .get();
    }
}
