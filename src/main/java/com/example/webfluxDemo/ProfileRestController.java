package com.example.webfluxDemo;

import org.reactivestreams.Publisher;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;

//SpringMVC style code
@RestController
@RequestMapping(value="/profiles", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileRestController {
    private final MediaType mediaType = MediaType.APPLICATION_JSON;
    private final ProfileService profileRepository;

    public ProfileRestController(ProfileService profileRepository) {
        this.profileRepository = profileRepository;
    }

    @GetMapping
    Publisher<Profile> getAll(){
        return this.profileRepository.all();
    }

    @GetMapping("/{id}")
    Publisher<Profile> getById(@PathVariable("id") String id) {
        return this.profileRepository.get(id);
    }
//	This method supports creating a new Profile with an HTTP POST action.
//	In this handler method we expect incoming requests to have a JSON body that the framework then marshals into a Java object, Profile.
//	This happens automatically based on the content-type of the incoming request and the configured, acceptable, convertible payloads supported by Spring WebFlux.
    @PostMapping
    Publisher<ResponseEntity<Profile>> create(@RequestBody Profile profile){
        return this.profileRepository
                .create(profile.getEmail(), profile.getName())
                .map(profile1 -> ResponseEntity.created(URI.create("/profiles/"+profile.getId()))
                        .contentType(mediaType)
                        .build());
    }

    @DeleteMapping("/{id}")
    Publisher<Profile> deleteById(@PathVariable String id){
        return this.profileRepository.delete(id);
    }

    @PutMapping("/{id}")
    Publisher<ResponseEntity<Profile>> updateById(@PathVariable String id, @RequestBody Profile profile){
        return Mono.just(profile).flatMap(p-> this.profileRepository.update(id, p.getEmail(), p.getName()))
                .map(p->ResponseEntity.ok().contentType(this.mediaType).build());
    }
}
