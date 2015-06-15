package io.spring.guides.bookmarks.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

import static io.spring.guides.bookmarks.config.OAuth2AuthorizationServerConfiguration.APPLICATION_NAME;
import static io.spring.guides.bookmarks.config.OAuth2AuthorizationServerConfiguration.SCOPE;
import static java.lang.String.format;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.requestMatchers()
                .antMatchers("/bookmarks", "/bookmarks/**")
                .and()
                .authorizeRequests()
                .anyRequest()
                .access(format("#oauth2.hasScope('%s')", SCOPE));
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(APPLICATION_NAME);
    }
}
