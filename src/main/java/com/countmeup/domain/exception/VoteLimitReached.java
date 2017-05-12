package com.countmeup.domain.exception;

import org.springframework.http.HttpStatus;

public class VoteLimitReached extends ApplicationException {
    public VoteLimitReached() {
        super("vote_limit_reached", HttpStatus.FORBIDDEN);
    }
}
