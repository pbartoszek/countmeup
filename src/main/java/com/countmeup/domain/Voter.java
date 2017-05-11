package com.countmeup.domain;

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
            return false;
        }

    }

    public boolean isVoteLimitReached() {
        return castedVotes >= 3;
    }

    public int getCastedVotes() {
        return castedVotes;
    }
}
