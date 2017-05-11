package com.countmeup.domain;

import java.util.concurrent.atomic.AtomicLong;

public class Candidate {
    private final String name;
    private AtomicLong votes = new AtomicLong();

    public Candidate(String name) {
        this.name = name;
    }

    public void addVote() {
        votes.incrementAndGet();
    }

    public long getVotes() {
        return votes.get();
    }

    public String getName() {
        return name;
    }
}
