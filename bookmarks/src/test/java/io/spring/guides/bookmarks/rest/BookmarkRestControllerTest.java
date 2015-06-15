package io.spring.guides.bookmarks.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.guides.bookmarks.Application;
import io.spring.guides.bookmarks.config.OAuth2AuthorizationServerConfiguration;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.oauth2.client.test.BeforeOAuth2Context;
import org.springframework.security.oauth2.client.test.OAuth2ContextConfiguration;
import org.springframework.security.oauth2.client.test.OAuth2ContextSetup;
import org.springframework.security.oauth2.client.test.RestTemplateHolder;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.password.ResourceOwnerPasswordResourceDetails;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.client.RestOperations;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.oauth2.common.OAuth2AccessToken.BEARER_TYPE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromMethodCall;
import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class BookmarkRestControllerTest implements RestTemplateHolder {
    private static final String USERNAME = "jlong";
    private static final String PASSWORD = "password";
    @Rule
    public OAuth2ContextSetup oAuth2ContextSetup = OAuth2ContextSetup.standard(this);
    private MediaType contentType = new MediaType("application", "hal+json");
    private MockMvc mockMvc;
    private RestOperations restTemplate;
    private List<Bookmark> bookmarkList = new ArrayList<>();
    @Autowired
    private BookmarkRepository bookmarkRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private FilterChainProxy springSecurityFilterChain;
    private Account account;

    @BeforeOAuth2Context
    public void setupOAuth2() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(springSecurityFilterChain)
                .build();
        oAuth2ContextSetup.setAccessTokenProvider(new ResourceOwnerPasswordAccessTokenProvider() {{
            setRequestFactory(new MyMockClientHttpRequestFactory(mockMvc));
        }});
    }

    @Before
    public void setUp() {
        bookmarkRepository.deleteAllInBatch();
        accountRepository.deleteAllInBatch();
        account = accountRepository.save(new Account(USERNAME, PASSWORD));
        bookmarkList.add(bookmarkRepository.save(new Bookmark(
                account,
                format("http://bookmark.com/1/%s", USERNAME),
                "A description")));
        bookmarkList.add(bookmarkRepository.save(new Bookmark(
                account,
                format("http://bookmark.com/2/%s", USERNAME),
                "A description")));
    }

    @Test
    @OAuth2ContextConfiguration(ResourceOwner.class)
    public void readSingleBookmark() throws Exception {
        Long bookmarkId = bookmarkList.get(0).getId();
        String uri = fromMethodCall(on(BookmarkRestController.class).readBookmark(null, bookmarkId)).toUriString();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", format("%s %s", BEARER_TYPE, oAuth2ContextSetup.getAccessToken()));
        httpHeaders.setAccept(Collections.singletonList(contentType));
        mockMvc.perform(get(uri).headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.bookmark.uri", is(format("http://bookmark.com/1/%s", USERNAME))))
                .andExpect(jsonPath("$.bookmark.description", is("A description")))
                .andExpect(jsonPath("$._links.self.href", containsString(format("/bookmarks/%d", bookmarkId))));
    }

    @Test
    @OAuth2ContextConfiguration(ResourceOwner.class)
    public void readBookmarks() throws Exception {
        String uri = fromMethodCall(on(BookmarkRestController.class).readBookmarks(null)).toUriString();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", format("%s %s", BEARER_TYPE, oAuth2ContextSetup.getAccessToken()));
        httpHeaders.setAccept(Collections.singletonList(contentType));
        mockMvc.perform(get(uri).headers(httpHeaders))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$._embedded.bookmarkResources", hasSize(2)))
                .andExpect(jsonPath("$._embedded.bookmarkResources[0].bookmark.uri",
                        is(format("http://bookmark.com/1/%s", USERNAME))))
                .andExpect(jsonPath("$._embedded.bookmarkResources[0].bookmark.description", is("A description")))
                .andExpect(jsonPath("$._embedded.bookmarkResources[1].bookmark.uri",
                        is(format("http://bookmark.com/2/%s", USERNAME))))
                .andExpect(jsonPath("$._embedded.bookmarkResources[1].bookmark.description", is("A description")));
    }

    @Test
    @OAuth2ContextConfiguration(ResourceOwner.class)
    public void add() throws Exception {
        Bookmark b = new Bookmark(account, "http://spring.io", "The best resource for Spring news and information");
        String bookmarkJson = json(b);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", format("%s %s", BEARER_TYPE, oAuth2ContextSetup.getAccessToken()));
        httpHeaders.setAccept(Collections.singletonList(contentType));
        String uri = fromMethodCall(on(BookmarkRestController.class).add(null, b)).toUriString();
        mockMvc.perform(post(uri).headers(httpHeaders).contentType(contentType).content(bookmarkJson))
                .andExpect(status().isCreated());
    }

    protected String json(Object o) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        MockHttpOutputMessage outputMessage = new MockHttpOutputMessage();
        objectMapper.writeValue(outputMessage.getBody(), o);
        return outputMessage.getBodyAsString();
    }

    @Override
    public RestOperations getRestTemplate() {
        return restTemplate;
    }

    @Override
    public void setRestTemplate(RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }

    public static class ResourceOwner extends ResourceOwnerPasswordResourceDetails {
        public ResourceOwner() throws HttpRequestMethodNotSupportedException {
            setClientId(OAuth2AuthorizationServerConfiguration.CLIENT_ID);
            setUsername(USERNAME);
            setPassword(PASSWORD);
            setClientSecret(OAuth2AuthorizationServerConfiguration.CLIENT_SECRET);
            setScope(Collections.singletonList(OAuth2AuthorizationServerConfiguration.SCOPE));
            setAccessTokenUri(fromMethodCall(on(TokenEndpoint.class).getAccessToken(null, null)).toUriString());
        }
    }
}
