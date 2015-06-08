package io.spring.guides.async_method;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.Future;

@Service
public class FacebookLookupService {
    RestTemplate restTemplate = new RestTemplate();

    @Async
    public Future<Page> findPage(String page) throws InterruptedException {
        Thread.sleep(1000L);
        return new AsyncResult<>(restTemplate.getForObject("http://graph.facebook.com/" + page, Page.class));
    }
}
