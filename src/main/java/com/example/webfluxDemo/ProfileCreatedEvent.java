package com.example.webfluxDemo;

import org.springframework.context.ApplicationEvent;

public class ProfileCreatedEvent extends ApplicationEvent {
    public ProfileCreatedEvent(Profile profile) {
        super(profile);
    }
}
