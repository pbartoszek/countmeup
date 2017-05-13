package com.countmeup.domain;

import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResultService {

    private final CandidateRepository candidateRepository;

    public ResultService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    public Map<String, Long> getResults() {
        return candidateRepository.getAll().stream()
            .sorted(Comparator.comparing(Candidate::getName))
            .collect(Collectors.toMap(Candidate::getName, Candidate::getVotes));
    }
}
