package com.countmeup.domain.exception;

import org.springframework.http.HttpStatus;

public class VoterNotFound extends ApplicationException {
    public VoterNotFound() {
        super("voter_not_found", HttpStatus.NOT_FOUND);
    }
}
