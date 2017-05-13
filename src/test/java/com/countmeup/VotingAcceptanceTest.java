package com.countmeup;

import com.countmeup.controller.VoteCommand;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.data.MapEntry;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {Application.class})
public class VotingAcceptanceTest {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void count_me_up_validates_parameters() throws Exception {
        registerVote(createVoteCommand(null, "A"), HttpStatus.INTERNAL_SERVER_ERROR);
        registerVote(createVoteCommand("A", null), HttpStatus.INTERNAL_SERVER_ERROR);
        registerVote(createVoteCommand("", "AC"), HttpStatus.INTERNAL_SERVER_ERROR);
        registerVote(createVoteCommand("A", ""), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DirtiesContext
    @Test
    public void count_me_up_accepts_a_vote() throws Exception {
        registerVote(createVoteCommand("A", "A"), HttpStatus.CREATED);

        Map<String, Long> results = getResults();
        assertThat(results).contains(MapEntry.entry("A", 1L));
    }

    @DirtiesContext
    @Test
    public void count_me_up_accepts_3_votes_per_user() throws Exception {
        VoteCommand voteCommand = createVoteCommand("A", "B");
        registerVote(voteCommand, HttpStatus.CREATED);
        registerVote(voteCommand, HttpStatus.CREATED);
        registerVote(voteCommand, HttpStatus.CREATED);

        registerVote(voteCommand, HttpStatus.FORBIDDEN);

        Map<String, Long> results = getResults();
        assertThat(results).contains(MapEntry.entry("A", 3L));
    }

    @DirtiesContext
    @Test
    public void count_me_up_accepts_3_votes_per_user_regardless_of_candidate() throws Exception {
        registerVote(createVoteCommand("A", "B"), HttpStatus.CREATED);
        registerVote(createVoteCommand("A", "B"), HttpStatus.CREATED);
        registerVote(createVoteCommand("D", "B"), HttpStatus.CREATED);

        registerVote(createVoteCommand("D", "B"), HttpStatus.FORBIDDEN);

        Map<String, Long> results = getResults();
        assertThat(results).contains(MapEntry.entry("A", 2L));
        assertThat(results).contains(MapEntry.entry("D", 1L));
    }

    @Test
    public void count_me_up_returns_the_voting_results() throws Exception {

        registerVotesForCandidate("A", 10);
        registerVotesForCandidate("B", 20);
        registerVotesForCandidate("C", 15);
        registerVotesForCandidate("D", 10);

        Map<String, Long> results = getResults();
        assertThat(results).contains(MapEntry.entry("A", 10L));
        assertThat(results).contains(MapEntry.entry("B", 20L));
        assertThat(results).contains(MapEntry.entry("C", 15L));
        assertThat(results).contains(MapEntry.entry("D", 10L));
    }


    private VoteCommand createVoteCommand(String candidate, String voter) {
        VoteCommand voteCommand = new VoteCommand();
        voteCommand.setCandidate(candidate);
        voteCommand.setVoter(voter);
        return voteCommand;
    }

    private void registerVotesForCandidate(String candidate, long votes) throws ExecutionException, InterruptedException {
        LongStream.rangeClosed(1, votes)
            .parallel()
            .forEach(voteNumber -> {
                try {
                    registerVote(createVoteCommand(candidate, candidate + voteNumber), HttpStatus.CREATED);
                } catch (Exception | AssertionError e) {
                    throw new RuntimeException(
                        String.format("Exception when registering vote %s for candidate %s", voteNumber, candidate), e);
                }
            });
    }

    private void registerVote(VoteCommand voteCommand, HttpStatus expectedStatus) throws Exception {
        this.mockMvc.perform(post("/vote")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(asJson(voteCommand)))
            .andExpect(status().is(expectedStatus.value()));
    }

    private Map<String, Long> getResults() throws Exception {
        String responseContent = this.mockMvc.perform(get("/results")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse().getContentAsString();
        return objectMapper.readValue(responseContent, new TypeReference<Map<String, Long>>() {
        });
    }

    private String asJson(VoteCommand voteCommand) throws JsonProcessingException {
        return objectMapper.writeValueAsString(voteCommand);
    }
}
