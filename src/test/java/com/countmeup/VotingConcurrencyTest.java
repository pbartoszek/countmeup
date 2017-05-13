package com.countmeup;

import com.countmeup.controller.VoteCommand;
import com.countmeup.domain.ResultService;
import com.countmeup.domain.VotingService;
import com.countmeup.domain.exception.VoteLimitReached;
import org.assertj.core.data.MapEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {Application.class})
public class VotingConcurrencyTest {

    @Autowired
    private ResultService resultService;

    @Autowired
    private VotingService votingService;

    private ExecutorService executorService;

    @Before
    public void setUp() throws Exception {
        executorService = Executors.newFixedThreadPool(100);
    }

    @After
    public void tearDown() {
        executorService.shutdown();
    }

    @DirtiesContext
    @Test
    public void count_me_up_accepts_3_votes_per_user() throws Exception {
        List<Future<Void>> futures = new ArrayList<>(100);
        for (long voteNumber = 1; voteNumber <= 100; voteNumber++) {
            futures.add(executorService.submit(() -> {
                votingService.registerVote(createVoteCommand("A", "A"));
                return null;
            }));
        }

        int voteLimitReachedCount = 0;
        for (Future<Void> future : futures) {
            try {
                future.get();
            } catch (ExecutionException e) {
                if (e.getCause() instanceof VoteLimitReached)
                    voteLimitReachedCount++;
            }
        }

        assertThat(resultService.getResults()).contains(MapEntry.entry("A", 3L));
        assertThat(voteLimitReachedCount).isEqualTo(97);// 100-3

    }


    @DirtiesContext
    @Test
    public void count_me_up_returns_the_voting_results() throws Exception {
        List<Future<Void>> futures = new ArrayList<>(50_000);

        futures.addAll(registerVotesForCandidate("A", 15_000));
        futures.addAll(registerVotesForCandidate("B", 10_000));
        futures.addAll(registerVotesForCandidate("C", 5_000));
        futures.addAll(registerVotesForCandidate("D", 20_000));

        for (Future<Void> future : futures) {
            future.get();
        }

        Map<String, Long> results = resultService.getResults();
        assertThat(results).contains(MapEntry.entry("A", 15_000L));
        assertThat(results).contains(MapEntry.entry("B", 10_000L));
        assertThat(results).contains(MapEntry.entry("C", 5_000L));
        assertThat(results).contains(MapEntry.entry("D", 20_000L));
    }

    private VoteCommand createVoteCommand(String candidate, String voter) {
        VoteCommand voteCommand = new VoteCommand();
        voteCommand.setCandidate(candidate);
        voteCommand.setVoter(voter);
        return voteCommand;
    }

    private List<Future<Void>> registerVotesForCandidate(String candidate, long votes) throws ExecutionException, InterruptedException {
        List<Future<Void>> futures = new ArrayList<>((int) Long.min(votes, Integer.MAX_VALUE));
        for (long voteNumber = 1; voteNumber <= votes; voteNumber++) {
            futures.add(executorService.submit(registerVoteAsync(candidate, voteNumber)));
        }
        return futures;
    }

    private Callable<Void> registerVoteAsync(String candidate, long voteNumber) {
        return () -> {
            try {
                votingService.registerVote(createVoteCommand(candidate, candidate + voteNumber));
            } catch (Exception | AssertionError e) {
                throw new RuntimeException(
                    String.format("Exception when registering vote %s for candidate %s", voteNumber, candidate), e);
            }
            return null;
        };
    }
}
