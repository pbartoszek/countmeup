package com.countmeup.domain;

import java.util.Optional;

public interface VoterRepository {
    Optional<Voter> byName(String name);
}
