package com.countmeup.domain;

import com.countmeup.controller.VoteCommand;
import com.countmeup.domain.exception.CandidateNotFound;
import com.countmeup.domain.exception.VoteLimitReached;
import com.countmeup.domain.exception.VoterNotFound;
import org.springframework.stereotype.Service;

@Service
public class VotingService {
    private final VoterRepository voterRepository;
    private final CandidateRepository candidateRepository;

    public VotingService(VoterRepository voterRepository, CandidateRepository candidateRepository) {
        this.voterRepository = voterRepository;
        this.candidateRepository = candidateRepository;
    }

    public void registerVote(VoteCommand voteCommand) {
        Voter voter = voterRepository.byName(voteCommand.getVoter())
            .orElseThrow(VoterNotFound::new);
        Candidate candidate = candidateRepository.byName(voteCommand.getCandidate())
            .orElseThrow(CandidateNotFound::new);

        synchronized (voter) {
            //assuming that repository always returns the same object instance so synchronization works
            if (voter.isVoteLimitReached()) {
                throw new VoteLimitReached();
            }
            //normally this would run in a database transaction so these two operations would be atomic
            voter.increaseCastedVotes();
            candidate.addVote();
        }
    }
}
