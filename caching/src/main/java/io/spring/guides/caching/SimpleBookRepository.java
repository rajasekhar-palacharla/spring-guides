package io.spring.guides.caching;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class SimpleBookRepository implements BookRepository {
    @Override
    @Cacheable("books")
    public Book getByIsbn(String isbn) {
        simulateSlowService();
        return new Book("isbn", "Some very interesting book");
    }

    private void simulateSlowService() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
}
