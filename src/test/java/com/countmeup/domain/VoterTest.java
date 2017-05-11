package com.countmeup.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class VoterTest {

    @Test
    public void shouldReturnZeroCastedVotesForNewVoter() throws Exception {
        //when
        Voter voter = new Voter("A");
        //then
        assertThat(voter.getCastedVotes()).isEqualTo(0);
        assertThat(voter.isVoteLimitReached()).isFalse();
    }

    @Test
    public void shouldIncreaseVotesIfNotReachedLimit() throws Exception {
        //given
        Voter voter = new Voter("A");
        //when
        voter.increaseCastedVotes();
        voter.increaseCastedVotes();
        voter.increaseCastedVotes();
        //then
        assertThat(voter.getCastedVotes()).isEqualTo(3);
        assertThat(voter.isVoteLimitReached()).isTrue();
    }

    @Test
    public void shouldNotIncreaseVotesIfReachedLimit() throws Exception {
        //given
        Voter voter = new Voter("A");
        //when
        voter.increaseCastedVotes();
        voter.increaseCastedVotes();
        voter.increaseCastedVotes();
        voter.increaseCastedVotes();
        //then
        assertThat(voter.getCastedVotes()).isEqualTo(3);
    }
}