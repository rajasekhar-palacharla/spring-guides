package io.spring.guides.bookmarks.rest;

import io.spring.guides.bookmarks.hateoas.BookmarkResource;
import io.spring.guides.bookmarks.model.AccountRepository;
import io.spring.guides.bookmarks.model.Bookmark;
import io.spring.guides.bookmarks.model.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.security.Principal;
import java.util.stream.Collectors;

/*
 * curl -X POST -vu ios-bookmarks:123456 http://localhost:8080/oauth/token -H "Accept: application/json"
 * -d "password=password&username=jlong&grant_type=password&scope=write&client_secret=123456&client_id=ios-bookmarks"
 *
 * curl -X GET http://127.0.0.1:8080/bookmarks -H "Authorization: Bearer <access_token>"
 */
@RestController
@RequestMapping("/bookmarks")
public class BookmarkRestController {
    private BookmarkRepository bookmarkRepository;
    private AccountRepository accountRepository;

    @Autowired
    public BookmarkRestController(BookmarkRepository bookmarkRepository, AccountRepository accountRepository) {
        this.bookmarkRepository = bookmarkRepository;
        this.accountRepository = accountRepository;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> add(Principal principal, @RequestBody Bookmark bookmark) {
        String username = principal.getName();
        validateUser(username);
        return accountRepository.findByUsername(username).map(account -> {
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
    public Bookmark readBookmark(Principal principal, @PathVariable Long bookmarkId) {
        validateUser(principal.getName());
        return bookmarkRepository.findOne(bookmarkId);
    }


    @RequestMapping(method = RequestMethod.GET)
    public Resources<BookmarkResource> readBookmarks(Principal principal) {
        String username = principal.getName();
        validateUser(username);
        return new Resources<>(
                bookmarkRepository
                        .findByAccountUsername(username)
                        .stream()
                        .map(BookmarkResource::new)
                        .collect(Collectors.toList()));
    }

    private void validateUser(String username) {
        accountRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    }
}
