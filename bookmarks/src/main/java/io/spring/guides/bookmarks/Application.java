package io.spring.guides.bookmarks;

import io.spring.guides.bookmarks.model.Account;
import io.spring.guides.bookmarks.model.AccountRepository;
import io.spring.guides.bookmarks.model.Bookmark;
import io.spring.guides.bookmarks.model.BookmarkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class Application implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    BookmarkRepository bookmarkRepository;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Arrays.stream("jhoeller,dsyer,pwebb,ogierke,rwinch,mfisher,mpollack,jlong".split(",")).forEach(username -> {
            Account account = accountRepository.save(new Account(username, "password"));
            bookmarkRepository.save(new Bookmark(
                    account,
                    String.format("http://bookmark.com/1/%s", username),
                    "A description"));
            LOGGER.info("Bookmarks was added for {}", username);
            bookmarkRepository.save(new Bookmark(
                    account,
                    String.format("http://bookmark.com/2/%s", username),
                    "A description"));
            LOGGER.info("Bookmarks was added for {}", username);
        });
    }
}
