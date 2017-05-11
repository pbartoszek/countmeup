package com.countmeup.domain.exception;

import org.springframework.http.HttpStatus;

public class CandidateNotFound extends ApplicationException {
    public CandidateNotFound() {
        super("candidate_not_found", HttpStatus.NOT_FOUND);
    }
}
