package spring.functionalreactive;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.Instant;

import static org.springframework.web.reactive.function.server.RouterFunctions.route;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@SpringBootApplication
public class FunctionalReactiveApplication {
//    functional reactive
    @Bean
    RouterFunction<ServerResponse> routes1(GreetingService greetingService) {
        return route().GET("/functionalStyleGreeting/{name}", new HandlerFunction<ServerResponse>() {
            @Override
            public Mono<ServerResponse> handle(ServerRequest serverRequest) {
                Mono<GreetingResponse> greet = greetingService.greet(new GreetingRequest(serverRequest.pathVariable("name")));
                return ok().body(greet, GreetingResponse.class);
            }
        }).build();
    }
// concise functional reactive
    @Bean
    RouterFunction<ServerResponse> routes2(GreetingService greetingService) {
        return route().GET("/conciseFunctionalStyleGreeting/{name}", hf ->
                ok().body(greetingService.greet(new GreetingRequest(hf.pathVariable("name"))),
                        GreetingResponse.class)
        ).build();

    }

    public static void main(String[] args) {
        SpringApplication.run(FunctionalReactiveApplication.class, args);
    }

}
//
@RestController
class GreetingController {
    private final GreetingService greetingService;

    GreetingController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/greeting/{name}")
    Mono<GreetingResponse> greet(@PathVariable String name) {
        return this.greetingService.greet(new GreetingRequest(name));
    }
}

@Service
class GreetingService {
    private GreetingResponse greet(String name) {
        return new GreetingResponse("Hello " + name + "@" + Instant.now());
    }

    Mono<GreetingResponse> greet(GreetingRequest request) {
        return Mono.just(greet(request.getName()));
    }

}

@Data
@AllArgsConstructor
@NoArgsConstructor
class GreetingResponse {
    private String message;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
class GreetingRequest {
    private String name;
}