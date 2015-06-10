package io.spring.guides.serving_mobile_web_content;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GreetingController {
    @RequestMapping("/greeting")
    public String greeting() {
        return "greeting";
    }
}
