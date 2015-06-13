package io.spring.guides.bookmarks.config;

import io.spring.guides.bookmarks.model.AccountRepository;
import io.spring.guides.bookmarks.rest.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
public class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
    @Autowired
    AccountRepository accountRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Bean
    UserDetailsService userDetailsService() {
        return username ->
                accountRepository
                        .findByUsername(username)
                        .map(account -> new User(
                                account.getUsername(), account.getPassword(), true, true, true, true,
                                AuthorityUtils.createAuthorityList("USERS")))
                        .orElseThrow(() -> new UserNotFoundException(
                                String.format("Could not find the user '%s'", username)));
    }
}
