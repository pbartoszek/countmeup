package com.countmeup.controller;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class VoteCommand {
    @NotNull
    @Size(min = 1)
    private String candidate;
    @NotNull
    @Size(min = 1)
    private String voter;

    public String getCandidate() {
        return candidate;
    }

    public void setCandidate(String candidate) {
        this.candidate = candidate;
    }

    public String getVoter() {
        return voter;
    }

    public void setVoter(String voter) {
        this.voter = voter;
    }
}