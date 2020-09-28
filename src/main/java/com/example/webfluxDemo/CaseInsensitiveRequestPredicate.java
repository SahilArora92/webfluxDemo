package com.example.webfluxDemo;

import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.ServerRequest;

public class CaseInsensitiveRequestPredicate implements RequestPredicate {

    private final RequestPredicate target;
    public CaseInsensitiveRequestPredicate(RequestPredicate target) {
        this.target=target;
    }

    @Override
    public boolean test(ServerRequest serverRequest) {
        return this.target.test(new LowerCaseUriServerRequestWrapper(serverRequest));
    }

    @Override
    public String toString() {
        return this.target.toString();
    }
}
