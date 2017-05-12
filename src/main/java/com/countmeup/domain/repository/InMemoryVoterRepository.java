package com.countmeup.domain.repository;

import com.countmeup.domain.Voter;
import com.countmeup.domain.VoterRepository;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class InMemoryVoterRepository implements VoterRepository {
    private Map<String, Voter> voters = new ConcurrentHashMap<>(1000);

    @Override
    public Optional<Voter> byName(String name) {

        Voter voter = voters.get(name);
        if (voter == null) {
            //put if absent used to eliminate race conditions
            voters.putIfAbsent(name, new Voter(name));
            return Optional.of(voters.get(name));
        }
        return Optional.of(voter);
    }
}
