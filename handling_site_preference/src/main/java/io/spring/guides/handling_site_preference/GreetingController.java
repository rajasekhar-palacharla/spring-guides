package io.spring.guides.handling_site_preference;

import org.springframework.mobile.device.site.SitePreference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class GreetingController {
    @RequestMapping("/site-preference")
    public
    @ResponseBody
    String home(SitePreference sitePreference) {
        if (sitePreference != null) {
            return String.format("Hello %s site preference!", sitePreference);
        } else {
            return "Site preferences is not configured!";
        }
    }
}
