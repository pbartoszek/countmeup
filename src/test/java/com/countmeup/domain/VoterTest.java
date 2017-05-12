package com.countmeup.domain;

import com.countmeup.domain.exception.VoteLimitReached;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
    public void shouldThrowExceptionIfReachedLimitAndTryingIncreaseVotes() throws Exception {
        //given
        Voter voter = new Voter("A");
        //when
        voter.increaseCastedVotes();
        voter.increaseCastedVotes();
        voter.increaseCastedVotes();
        assertThatThrownBy(voter::increaseCastedVotes).isInstanceOf(VoteLimitReached.class);
        //then
        assertThat(voter.getCastedVotes()).isEqualTo(3);
    }
}