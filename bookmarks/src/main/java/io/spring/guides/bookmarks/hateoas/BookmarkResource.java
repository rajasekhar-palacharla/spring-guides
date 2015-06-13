package io.spring.guides.bookmarks.hateoas;

import io.spring.guides.bookmarks.model.Bookmark;
import io.spring.guides.bookmarks.rest.BookmarkRestController;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class BookmarkResource extends ResourceSupport {
    private final Bookmark bookmark;

    public BookmarkResource(Bookmark bookmark) {
        this.bookmark = bookmark;
        add(new Link(bookmark.getUri(), "bookmark-uri"));
        add(linkTo(methodOn(BookmarkRestController.class).readBookmarks(null)).withRel("bookmarks"));
        add(linkTo(methodOn(BookmarkRestController.class).readBookmark(null, bookmark.getId())).withSelfRel());
    }
}
