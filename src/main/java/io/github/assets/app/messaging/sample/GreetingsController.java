package io.github.assets.app.messaging.sample;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingsController {

    private final GreetingsService greetingsService;

    public GreetingsController(final GreetingsService greetingsService) {
        this.greetingsService = greetingsService;
    }

    @GetMapping("/greetings")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void greetings(@RequestParam("message") String greeting) {

        Greetings greetings = Greetings.builder().message(greeting).timestamp(System.currentTimeMillis()).build();

        greetingsService.sendGreeting(greetings);
    }
}
