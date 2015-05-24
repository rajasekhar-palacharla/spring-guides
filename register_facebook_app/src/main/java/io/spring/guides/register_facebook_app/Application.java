package io.spring.guides.register_facebook_app;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.social.facebook.api.impl.FacebookTemplate;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;

import javax.swing.*;

public class Application {
    public static void main(String[] args) {
        String appId = promptForInput("Enter your App ID:");
        String appSecret = promptForInput("Enter your App Secret:");
        String accessToken = fetchApplicationAccessToken(appId, appSecret);
        AppDetails appDetails = fetchApplicationData(appId, accessToken);
        System.out.println("Application Details");
        System.out.println("-------------");
        System.out.println("ID:                 " + appDetails.id);
        System.out.println("Name:               " + appDetails.name);
        System.out.println("Namespace:          " + appDetails.namespace);
        System.out.println("Contact Email:      " + appDetails.contactEmail);
        System.out.println("Website URL:        " + appDetails.websiteUrl);
    }

    private static AppDetails fetchApplicationData(String appId, String appToken) {
        return new FacebookTemplate(appToken).restOperations().getForObject(
                "https://graph.facebook.com/{appId}?fields=name,namespace,contact_email,website_url",
                AppDetails.class,
                appId
        );
    }

    private static String fetchApplicationAccessToken(String appId, String appSecret) {
        return new FacebookConnectionFactory(appId, appSecret)
                .getOAuthOperations().authenticateClient().getAccessToken();
    }

    private static String promptForInput(String promptText) {
        return JOptionPane.showInputDialog(promptText + " ");
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static final class AppDetails {
        private long id;
        private String name;
        private String namespace;
        @JsonProperty("contact_email")
        private String contactEmail;
        @JsonProperty("website_url")
        private String websiteUrl;
    }
}
