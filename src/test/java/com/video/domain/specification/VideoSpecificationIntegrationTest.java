package com.video.domain.specification;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.video.domain.Video;
import com.video.domain.repository.VideoRepository;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class VideoSpecificationIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VideoRepository videoRepository;

    @Test
    void shouldGetByTitlePhrase() {

        entityManager.persist(Video.create("minPath1", "filePath1", "abcdefgh", 1990,
                "desc", Set.of(), Set.of(), Set.of(), Set.of()));
        entityManager.persist(Video.create("minPath2", "filePath2", "abcdefg", 1990,
                "desc", Set.of(), Set.of(), Set.of(), Set.of()));
        entityManager.persist(Video.create("minPath3", "filePath3", "zzzzzzfg", 1990,
                "desc", Set.of(), Set.of(), Set.of(), Set.of()));
        entityManager.persist(Video.create("minPath4", "filePath4", "abcdef", 1990,
                "desc", Set.of(), Set.of(), Set.of(), Set.of()));
        entityManager.persist(Video.create("minPath5", "filePath5", "abcde", 1990,
                "desc", Set.of(), Set.of(), Set.of(), Set.of()));
        entityManager.persist(Video.create("minPath6", "filePath6", "abcd", 1990,
                "desc", Set.of(), Set.of(), Set.of(), Set.of()));
        entityManager.persist(Video.create("minPath7", "filePath7", "abc", 1990,
                "desc", Set.of(), Set.of(), Set.of(), Set.of()));
        entityManager.persist(Video.create("minPath8", "filePath8", "ab", 1990,
                "desc", Set.of(), Set.of(), Set.of(), Set.of()));
        entityManager.persist(Video.create("minPath9", "filePath9", "a", 1990,
                "desc", Set.of(), Set.of(), Set.of(), Set.of()));
        entityManager.flush();

        assertThat(videoRepository.findAll(VideoSpecification.byTitlePhrase("a"))).hasSize(8);
        assertThat(videoRepository.findAll(VideoSpecification.byTitlePhrase("ab"))).hasSize(7);
        assertThat(videoRepository.findAll(VideoSpecification.byTitlePhrase("bcd"))).hasSize(5);
        assertThat(videoRepository.findAll(VideoSpecification.byTitlePhrase("fg"))).hasSize(3);
        assertThat(videoRepository.findAll(VideoSpecification.byTitlePhrase("fgh"))).hasSize(1);
        assertThat(videoRepository.findAll(VideoSpecification.byTitlePhrase("abcdefgh"))).hasSize(1);
    }
}
