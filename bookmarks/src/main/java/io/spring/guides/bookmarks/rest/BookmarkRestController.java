package io.spring.guides.bookmarks.rest;

import io.spring.guides.bookmarks.model.AccountRepository;
import io.spring.guides.bookmarks.model.Bookmark;
import io.spring.guides.bookmarks.model.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;

@RestController
@RequestMapping("/{username}/bookmarks")
public class BookmarkRestController {
    private BookmarkRepository bookmarkRepository;
    private AccountRepository accountRepository;

    @Autowired
    public BookmarkRestController(BookmarkRepository bookmarkRepository, AccountRepository accountRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.accountRepository = accountRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> add(@PathVariable String username, @RequestBody Bookmark bookmark) {
        this.validateUser(username);
        return this.accountRepository.findByUsername(username).map(account -> {
            Bookmark b = bookmarkRepository.save(new Bookmark(account, bookmark.getUri(), bookmark.getDescription()));
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(b.getId()).toUri());
            return new ResponseEntity<>(null, httpHeaders, HttpStatus.CREATED);
        }).get();
    }

    @RequestMapping(value = "/{bookmarkId}", method = RequestMethod.GET)
    public Bookmark readBookmark(@PathVariable String username, @PathVariable Long bookmarkId) {
        this.validateUser(username);
        return this.bookmarkRepository.findOne(bookmarkId);
    }


    @RequestMapping(method = RequestMethod.GET)
    public Collection<Bookmark> readBookmarks(@PathVariable String username) {
        this.validateUser(username);
        return bookmarkRepository.findByAccountUsername(username);
    }

    private void validateUser(String username) {
        this.accountRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }
}
