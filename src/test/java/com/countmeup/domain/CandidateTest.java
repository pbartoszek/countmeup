package com.countmeup.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class CandidateTest {
    @Test
    public void shouldIncreaseVotes() throws Exception {
        //given
        Candidate candidate = new Candidate("A");
        //when
        candidate.addVote();
        //then
        assertThat(candidate.getVotes()).isEqualTo(1);
    }


}