package com.example.webfluxDemo;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

//Repositories are responsible for persisting entities and value types.
//They present clients with a simple model for obtaining persistent objects and managing their life cycle.
interface ProfileRepository extends ReactiveMongoRepository<Profile, String> {
}
