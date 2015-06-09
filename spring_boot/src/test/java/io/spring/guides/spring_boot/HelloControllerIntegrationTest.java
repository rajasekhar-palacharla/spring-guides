package io.spring.guides.spring_boot;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({"server.port=0"})
public class HelloControllerIntegrationTest {

    @Value("${local.server.port}")
    private int port;
    private URL base;
    private RestTemplate restTemplate;

    @Before
    public void setUp() throws Exception {
        base = new URL(String.format("http://localhost:%d/", port));
        restTemplate = new TestRestTemplate();
    }

    @Test
    public void testIndex() throws Exception {
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(base.toString(), String.class);
        assertThat(responseEntity.getBody(), Matchers.equalTo("Greetings from Spring Boot!"));
    }
}
