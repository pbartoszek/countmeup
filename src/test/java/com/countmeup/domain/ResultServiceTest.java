package com.countmeup.domain;

import org.assertj.core.data.MapEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ResultServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    private ResultService underTest;

    @Before
    public void setUp() throws Exception {
        underTest = new ResultService(candidateRepository);
    }

    @Test(timeout = 1000 /* functional requirement*/)
    public void shouldReturnCorrectVotingResults() throws Exception {
        //given
        Candidate candidateA = createCandidate("A", 8_000_000);
        Candidate candidateB = createCandidate("B", 2_000_000);
        Candidate candidateC = createCandidate("C", 6_000_000);
        Candidate candidateD = createCandidate("D", 4_000_000);

        when(candidateRepository.getAll()).thenReturn(Arrays.asList(candidateA, candidateB, candidateC, candidateD));
        //when
        Map<String, Long> results = underTest.getResults();
        //then
        assertThat(results).contains(MapEntry.entry("A", 8_000_000L));
        assertThat(results).contains(MapEntry.entry("B", 2_000_000L));
        assertThat(results).contains(MapEntry.entry("C", 6_000_000L));
        assertThat(results).contains(MapEntry.entry("D", 4_000_000L));

    }

    private Candidate createCandidate(String a, int votes) {
        Candidate candidateA = new Candidate(a);
        registerVotes(candidateA, votes);
        return candidateA;
    }

    private void registerVotes(Candidate candidateA, long votes) {
        for (long l = 1; l <= votes; l++) {
            candidateA.addVote();
        }
    }
}