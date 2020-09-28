package com.example.webfluxDemo;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.context.annotation.Profile;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Log4j2
@Component
@Profile("demo")
public class ProfileDataInitializer implements ApplicationListener<ApplicationReadyEvent> {
    private final ProfileRepository repository;

    public ProfileDataInitializer(ProfileRepository repository) {
        this.repository = repository;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        repository.deleteAll()
                //we use Reactor’s Flux<T>.just(T..) factory method to create a new Publisher with a static list of String records, in-memory.
                .thenMany(
                        Flux.just("A","B","C","D")
                        .map(name -> new com.example.webfluxDemo.Profile(UUID.randomUUID().toString(), name + "@email.com", name))
                        .flatMap(repository::save)
                )
                .thenMany(repository.findAll())
                .subscribe(profile -> log.info("saving"+ profile.toString()));
    }
}
