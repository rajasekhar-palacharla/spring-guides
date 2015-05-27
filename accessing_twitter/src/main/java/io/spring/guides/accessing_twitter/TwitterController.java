package io.spring.guides.accessing_twitter;

import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;

@Controller
@RequestMapping("/")
public class TwitterController {
    private Twitter twitter;
    private ConnectionRepository repository;

    @Inject
    public TwitterController(Twitter twitter, ConnectionRepository repository) {
        this.twitter = twitter;
        this.repository = repository;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String helloTwitter(Model model) {
        if (repository.findPrimaryConnection(Twitter.class) == null) {
            return "redirect:/connect/twitter";
        }
        model.addAttribute(twitter.userOperations().getUserProfile());
        model.addAttribute("friends", twitter.friendOperations().getFriends());
        return "hello";
    }
}
