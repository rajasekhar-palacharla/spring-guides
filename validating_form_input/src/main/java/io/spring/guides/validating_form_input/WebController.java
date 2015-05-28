package io.spring.guides.validating_form_input;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.validation.Valid;

@Controller
public class WebController extends WebMvcConfigurerAdapter {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/results").setViewName("results");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String showForm(Person person) {
        return "form";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String checkPersonInfo(@Valid Person person, BindingResult result) {
        if (result.hasErrors()) {
            return "form";
        }
        return "redirect:/results";
    }
}
