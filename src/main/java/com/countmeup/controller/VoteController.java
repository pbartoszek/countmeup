package com.countmeup.controller;

import com.countmeup.domain.VotingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class VoteController {

    private final VotingService votingService;

    public VoteController(VotingService votingService) {
        this.votingService = votingService;
    }

    @PostMapping(value = "/vote",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity vote(@RequestBody @Valid VoteCommand voteCommand) {
        votingService.registerVote(voteCommand);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
