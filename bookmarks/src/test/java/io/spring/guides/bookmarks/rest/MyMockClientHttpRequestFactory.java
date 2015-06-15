package io.spring.guides.bookmarks.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.mock.http.client.MockClientHttpRequest;
import org.springframework.mock.http.client.MockClientHttpResponse;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.request;

class MyMockClientHttpRequestFactory implements ClientHttpRequestFactory {
    private final MockMvc mockMvc;

    public MyMockClientHttpRequestFactory(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Override
    public ClientHttpRequest createRequest(final URI uri, final HttpMethod httpMethod) throws IOException {
        return new MockClientHttpRequest(httpMethod, uri) {

            @Override
            public ClientHttpResponse executeInternal() throws IOException {
                try {
                    MockHttpServletRequestBuilder requestBuilder = request(httpMethod, uri.toString());
                    Arrays.stream(getBodyAsString().split("&"))
                            .map(s -> Arrays.stream(s.split("=")).map(String::trim).collect(Collectors.toList()))
                            .forEach(l -> requestBuilder.param(l.get(0), l.get(1)));
                    requestBuilder.headers(getHeaders());
                    MockHttpServletResponse servletResponse = mockMvc.perform(requestBuilder).andReturn().getResponse();

                    MockClientHttpResponse clientResponse = new MockClientHttpResponse(
                            servletResponse.getContentAsByteArray(),
                            HttpStatus.valueOf(servletResponse.getStatus()));
                    clientResponse.getHeaders().putAll(getResponseHeaders(servletResponse));

                    return clientResponse;
                } catch (Exception ex) {
                    return new MockClientHttpResponse(
                            ex.toString().getBytes("UTF-8"),
                            HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        };
    }

    private HttpHeaders getResponseHeaders(MockHttpServletResponse response) {
        HttpHeaders headers = new HttpHeaders();
        for (String name : response.getHeaderNames()) {
            List<String> values = response.getHeaders(name);
            for (String value : values) {
                headers.add(name, value);
            }
        }
        return headers;
    }
}
