package io.spring.guides.authenticating_ldap;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @RequestMapping(value = "/")
    public String index() {
        return "Welcome to the home page";
    }
}
