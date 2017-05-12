package com.countmeup.domain;

import java.util.Collection;
import java.util.Optional;

public interface CandidateRepository {
    Optional<Candidate> byName(String name);

    Collection<Candidate> getAll();
}
