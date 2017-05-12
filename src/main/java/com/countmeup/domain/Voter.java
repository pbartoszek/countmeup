package com.countmeup.domain;

import com.countmeup.domain.exception.VoteLimitReached;

public class Voter {
    private final String name;
    private int castedVotes;

    public Voter(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean increaseCastedVotes() {
        if (!isVoteLimitReached()) {
            castedVotes++;
            return true;
        } else {
            throw new VoteLimitReached();
        }

    }

    public boolean isVoteLimitReached() {
        return castedVotes >= 3;
    }

    public int getCastedVotes() {
        return castedVotes;
    }
}
