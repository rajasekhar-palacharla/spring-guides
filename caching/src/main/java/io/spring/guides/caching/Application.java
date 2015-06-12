package io.spring.guides.caching;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableCaching
public class Application implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    @Autowired
    private BookRepository bookRepository;

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("books");
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LOGGER.info("Fetching books...");
        LOGGER.info("isbn-1234 ------> {}", bookRepository.getByIsbn("isbn-1234"));
        LOGGER.info("isbn-1234 ------> {}", bookRepository.getByIsbn("isbn-1234"));
        LOGGER.info("isbn-1234 ------> {}", bookRepository.getByIsbn("isbn-1234"));
    }
}
