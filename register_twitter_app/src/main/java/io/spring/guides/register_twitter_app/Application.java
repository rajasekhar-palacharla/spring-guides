package io.spring.guides.register_twitter_app;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.social.oauth2.OAuth2Template;
import org.springframework.web.client.RestTemplate;

import javax.swing.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Application {
    public static void main(String[] args) {
        String appId = promptForInput("Enter your customer ID:");
        String appSecret = promptForInput("Enter your customer secret:");
        String accessToken = fetchApplicationAccessToken(appId, appSecret);
        searchTwitter("#springframework", accessToken).stream().forEach(System.out::println);
    }

    private static List<String> searchTwitter(String query, String appToken) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Bearer " + appToken);
        HttpEntity<String> httpEntity = new HttpEntity<>("", httpHeaders);
        Map result = restTemplate.exchange("https://api.twitter.com/1.1/search/tweets.json?q={query}",
                HttpMethod.GET, httpEntity, Map.class, query).getBody();
        List<Map<String, String>> statuses = (List<Map<String, String>>) result.get("statuses");
        return statuses.stream().map(status -> status.get("text")).collect(Collectors.toList());
    }

    private static String fetchApplicationAccessToken(String appId, String appSecret) {
        return new OAuth2Template(appId, appSecret, "", "https://api.twitter.com/oauth2/token")
                .authenticateClient().getAccessToken();
    }

    private static String promptForInput(String promptText) {
        return JOptionPane.showInputDialog(promptText + " ");
    }
}
