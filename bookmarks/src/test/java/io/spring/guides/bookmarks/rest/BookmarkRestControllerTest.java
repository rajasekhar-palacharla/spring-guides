package io.spring.guides.bookmarks.rest;

import io.spring.guides.bookmarks.Application;
import io.spring.guides.bookmarks.model.Account;
import io.spring.guides.bookmarks.model.AccountRepository;
import io.spring.guides.bookmarks.model.Bookmark;
import io.spring.guides.bookmarks.model.BookmarkRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.oauth2.client.test.OAuth2ContextSetup;
import org.springframework.security.oauth2.client.test.RestTemplateHolder;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestOperations;
import org.springframework.web.context.WebApplicationContext;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class BookmarkRestControllerTest implements RestTemplateHolder {
    @Rule
    public OAuth2ContextSetup oAuth2ContextSetup = OAuth2ContextSetup.standard(this);
    private MediaType contentType = new MediaType("application", "hal+json");
    private String username = "pavel";
    private String password = "password";
    private MockMvc mockMvc;
    private HttpMessageConverter httpMessageConverter;
    private Account account;
    private List<Bookmark> bookmarkList = new ArrayList<>();
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    private RestOperations restTemplate;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        httpMessageConverter = Arrays.stream(converters)
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .get();
    }

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(springSecurityFilterChain)
                .build();
        bookmarkRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();
        account = accountRepository.save(new Account(username, password));
        bookmarkList.add(bookmarkRepository.save(new Bookmark(
                account,
                String.format("http://bookmark.com/1/%s", username),
                "A description")));
        bookmarkList.add(bookmarkRepository.save(new Bookmark(
                account,
                String.format("http://bookmark.com/2/%s", username),
                "A description")));
    }

    @Test
    public void readSingleBookmark() throws Exception {
        Long bookmarkId = bookmarkList.get(0).getId();
        URI uri = fromMethodCall(on(BookmarkRestController.class).readBookmark(null, bookmarkId))
                .build()
                .encode()
                .toUri();
        mockMvc.perform(get(uri))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.bookmark.id", is(bookmarkId.intValue())))
                .andExpect(jsonPath("$.bookmark.uri", is(String.format("http://bookmark.com/1/%s", username))))
                .andExpect(jsonPath("$.bookmark.description", is("A description")))
                .andExpect(jsonPath("$._links.self.href",
                        containsString(String.format("/%s/bookmarks/%d", username, bookmarkId))));
    }

    @Override
    public RestOperations getRestTemplate() {
        return restTemplate;
    }

    @Override
    public void setRestTemplate(RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }
}
