package com.example.webfluxDemo;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;


@Component
public class ProfileHandler {
    private final ProfileService profileService;

    public ProfileHandler(ProfileService profileService) {
        this.profileService = profileService;
    }

    public Mono<ServerResponse> all(ServerRequest serverRequest) {
        return defaultReadResponse(this.profileService.all());
    }

    public Mono<ServerResponse> getById(ServerRequest serverRequest) {
        return defaultReadResponse(this.profileService.get(id(serverRequest)));
    }

    public Mono<ServerResponse> deleteById(ServerRequest serverRequest) {
        return defaultReadResponse(this.profileService.delete(id(serverRequest)));
    }

    public Mono<ServerResponse> create(ServerRequest serverRequest) {
        Flux<Profile> flux = serverRequest
                .bodyToFlux(Profile.class)
                .flatMap(profile -> this.profileService.create(profile.getEmail(), profile.getName()));
        return defaultWriteResponse(flux);
    }

    private static Mono<ServerResponse> defaultWriteResponse(Publisher<Profile> profiles) {
        return Mono.from(profiles)
                .flatMap(profile -> ServerResponse
                        .created(URI.create("/profiles/" + profile.getId()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .build()
                );
    }

    public Mono<ServerResponse> updateById(ServerRequest serverRequest) {
        Flux<Profile> id = serverRequest.bodyToFlux(Profile.class)
                .flatMap(profile -> this.profileService.update(id(serverRequest), profile.getEmail(), profile.getName()));
        return defaultReadResponse(id);
    }

    private static Mono<ServerResponse> defaultReadResponse(Publisher<Profile> profiles) {
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(profiles, Profile.class);
    }

    private static String id(ServerRequest r){
        return r.pathVariable("id");
    }
}
