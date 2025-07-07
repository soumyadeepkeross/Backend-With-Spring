package com.springCore.graphQL.publisher;


import com.springCore.graphQL.entity.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

@Component
public class SignupPublisher {
    private final Sinks.Many<User> sink;

    public SignupPublisher() {
        this.sink = Sinks.many().multicast().onBackpressureBuffer();
    }

    public void publish(User newUser) {
        sink.tryEmitNext(newUser);
    }

    public Flux<User> getPublisher() {
        return sink.asFlux();
    }
}
