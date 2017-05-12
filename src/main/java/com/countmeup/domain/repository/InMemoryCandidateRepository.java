package com.countmeup.domain.repository;

import com.countmeup.domain.Candidate;
import com.countmeup.domain.CandidateRepository;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class InMemoryCandidateRepository implements CandidateRepository {
    private final Map<String, Candidate> candidates = new HashMap<>();

    public InMemoryCandidateRepository() {
        candidates.put("A", new Candidate("A"));
        candidates.put("B", new Candidate("B"));
        candidates.put("C", new Candidate("C"));
        candidates.put("D", new Candidate("D"));
    }

    @Override
    public Optional<Candidate> byName(String name) {
        Candidate candidate = candidates.get(name);
        return Optional.ofNullable(candidate);
    }

    @Override
    public Collection<Candidate> getAll() {
        return Collections.unmodifiableCollection(candidates.values());
    }
}
