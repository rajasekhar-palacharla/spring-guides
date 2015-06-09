package io.spring.guides.messaging_stomp_websocket;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class GreetingController {
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(Message message) throws InterruptedException {
        Thread.sleep(3000);
        return new Greeting(String.format("Hello, %s!", message.getName()));
    }
}
