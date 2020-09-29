package com.example.webfluxDemo;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//will use methods from profileRepository layer which acts as our domain driven design
@Service
public class ProfileService {
    private final ApplicationEventPublisher publisher;
    private final ProfileRepository profileRepository;

    public ProfileService(ApplicationEventPublisher publisher, ProfileRepository profileRepository) {
        this.publisher = publisher;
        this.profileRepository = profileRepository;
    }

    public Flux<Profile> all(){
        return this.profileRepository.findAll();
    }

    public Mono<Profile> get(String id){
        return this.profileRepository.findById(id);
    }

    public Mono<Profile> update(String id, String email, String name){
        return this.profileRepository.findById(id)
                .map(p->new Profile(p.getId(), email, name))
                .flatMap(this.profileRepository::save);
    }

    public Mono<Profile> delete(String id){
        return this.profileRepository.findById(id)
                .flatMap(profile -> this.profileRepository.deleteById(profile.getId()).thenReturn(profile));
    }

    public Mono<Profile> create(String email, String name){
        return this.profileRepository
                .save(new Profile(null, email, name))
                .doOnSuccess(profile -> this.publisher.publishEvent(new ProfileCreatedEvent(profile)));
    }
}
