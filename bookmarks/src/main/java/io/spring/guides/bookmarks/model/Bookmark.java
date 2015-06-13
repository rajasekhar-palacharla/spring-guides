package io.spring.guides.bookmarks.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Bookmark {
    @Id
    @GeneratedValue
    private Long id;
    private String uri;
    private String description;
    @JsonIgnore
    @ManyToOne
    private Account account;

    public Bookmark() {
    }

    public Bookmark(Account account, String uri, String description) {
        this.account = account;
        this.uri = uri;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getUri() {
        return uri;
    }

    public String getDescription() {
        return description;
    }

    public Account getAccount() {
        return account;
    }
}
