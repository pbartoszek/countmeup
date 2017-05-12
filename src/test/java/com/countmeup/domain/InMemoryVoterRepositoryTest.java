package com.countmeup.domain;

import com.countmeup.domain.repository.InMemoryVoterRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class InMemoryVoterRepositoryTest {

    private InMemoryVoterRepository voterRepository = new InMemoryVoterRepository();

    @Test
    public void shouldReturnVoterByName() throws Exception {
        //given
        String name = "VoterA";
        //when
        Optional<Voter> voterOpt = voterRepository.byName(name);
        //then
        assertThat(voterOpt).isPresent();
        assertThat(voterOpt.get().getName()).isEqualTo(name);
    }
}