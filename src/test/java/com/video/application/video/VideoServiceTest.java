package com.video.application.video;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.util.ReflectionTestUtils;

import com.video.application.Mocks;
import com.video.application.exceptions.EntityObjectAlreadyExistsException;
import com.video.application.exceptions.EntityObjectNotFoundException;
import com.video.application.genre.GenreService;
import com.video.application.kafka.KafkaMessageProducer;
import com.video.application.person.PersonService;
import com.video.application.video.dto.AddVideoRequest;
import com.video.domain.Genre;
import com.video.domain.Person;
import com.video.domain.Video;
import com.video.domain.repository.VideoRepository;
import com.video.infrastructure.kafka.KafkaTopics;

@ExtendWith(MockitoExtension.class)
public class VideoServiceTest {

    @InjectMocks
    private VideoService videoService;

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private PersonService personService;

    @Mock
    private GenreService genreService;

    @Mock
    private KafkaMessageProducer kafkaMessageProducer;

    private static final String VIDEO_PATH = "videoPath";
    private static final String VIDEO_MINIATURES_PATH = "videoMiniaturesPath";

    private void whenVideoRepositorySaveThenAnswer() {
        when(videoRepository.save(Mockito.any(Video.class)))
                .thenAnswer(i -> i.getArgument(0));
    }

    private void videoExistsByFilePath(boolean bool) {
        when(videoRepository.existsByFilePath(Mockito.anyString()))
                .thenReturn(bool);
    }

    private void videoExistsById(boolean bool) {
        when(videoRepository.existsById(Mockito.anyLong()))
                .thenReturn(bool);
    }

    private void whenVideoRepositoryFindByIdThenReturn(Optional<Video> video) {
        when(videoRepository.findById(Mockito.anyLong()))
                .thenReturn(video);
    }

    private void whenVideoRepositoryDeleteByIdDoNothing(int size) {
        doNothing().when(videoRepository).deleteById(Mockito.anyLong());
    }

    private void whenVideoRepositoryFindAllBySpecificationTheReturn(int size) {
        when(videoRepository.findAll(Mockito.any(Specification.class)))
                .thenReturn(Mocks.videosWithEmptyCollectionsMock(size));
    }

    private void whenPersonServiceFindAllByIdsInThenAnswer() {
        when(personService.findAllByIdsIn(Mockito.anySet()))
                .thenAnswer(i -> Mocks.personsMock(((Set<?>) i.getArgument(0)).size()));
    }

    private void whenPersonServiceFindByIdThenReturn(Person person) throws EntityObjectNotFoundException {
        when(personService.findPersonById(Mockito.anyLong()))
                .thenReturn(person);
    }

    private void whenGenreServiceFindAllByIdsInThenAnswer() {
        when(genreService.findAllByIdsIn(Mockito.anySet()))
                .thenAnswer(i -> Mocks.genresMock(((Set<?>) i.getArgument(0)).size()));
    }

    private void whenGenreServiceFindByIdThenReturn(Genre genre) throws EntityObjectNotFoundException {
        when(genreService.findGenreById(Mockito.anyLong()))
                .thenReturn(genre);
    }

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(videoService, "videoPath", VIDEO_PATH);
        ReflectionTestUtils.setField(videoService, "videoMiniaturesPath", VIDEO_MINIATURES_PATH);
    }

    @Test
    void shouldAddVideo() throws EntityObjectAlreadyExistsException {
        whenVideoRepositorySaveThenAnswer();
        videoExistsByFilePath(false);
        whenGenreServiceFindAllByIdsInThenAnswer();
        whenPersonServiceFindAllByIdsInThenAnswer();

        AddVideoRequest request = AddVideoRequest.builder()
                .miniatureFileName("minName")
                .fileName("fName")
                .title("title")
                .year(1990)
                .description("desc")
                .genresIds(Set.of(1L, 2L))
                .castIds(Set.of(1L, 2L))
                .directorsIds(Set.of(1L, 2L))
                .screenwritersIds(Set.of(1L, 2L))
                .build();

        assertThat(videoService.addVideo(request)).isNotNull();
        verify(videoRepository, times(1)).save(
                Mockito.argThat(arg -> arg instanceof Video
                        && arg.getDirectors().size() == 2
                        && arg.getGenres().size() == 2
                        && arg.getScreenwriters().size() == 2
                        && arg.getCast().size() == 2));
        verify(kafkaMessageProducer, times(1)).sendMessage(Mockito.eq(KafkaTopics.VIDEO_ADDED),
                Mockito.anyString());
    }

    @Test
    void shouldNotAddVideoIfAlreadyExists() {
        videoExistsByFilePath(true);
        
        assertThrows(EntityObjectAlreadyExistsException.class,
                () -> videoService.addVideo(Mocks.addVideoRequestMock()),
                "Video already exists.");
        verify(videoRepository, never()).save(Mockito.any(Video.class));
        verify(kafkaMessageProducer, never()).sendMessage(Mockito.anyString(),
                Mockito.anyString());
    }

    @Test
    void shouldReturnTrueIfVideoExistsById() {
        videoExistsById(true);

        assertThat(videoService.videoExistsById(1L)).isTrue();
    }

    @Test
    void shouldReturnFalseIfVideoNotExistsById() {
        videoExistsById(false);

        assertThat(videoService.videoExistsById(1L)).isFalse();
    }

    @Test
    void shouldDeleteVideo() throws EntityObjectNotFoundException {
        videoExistsById(true);

        videoService.deleteVideo(1L);

        verify(videoRepository, times(1)).deleteById(Mockito.anyLong());
        verify(kafkaMessageProducer, times(1)).sendMessage(Mockito.eq(KafkaTopics.VIDEO_DELETED),
                Mockito.eq("1"));
    }

    @Test
    void shouldNotDeleteVideoIfNotExists() {
        videoExistsById(false);

        assertThrows(EntityObjectNotFoundException.class,
                () -> videoService.deleteVideo(1L),
                "Video not found.");
        verify(videoRepository, never()).deleteById(Mockito.anyLong());
        verify(kafkaMessageProducer, never()).sendMessage(Mockito.eq(KafkaTopics.VIDEO_DELETED),
                Mockito.anyString());
    }

    @Test
    void shouldGetVideoBasicData() throws EntityObjectNotFoundException {
        whenVideoRepositoryFindByIdThenReturn(Optional.of(Mocks.videoWithEmptyCollectionsMock()));

        assertThat(videoService.getVideoBasicData(1L)).isNotNull();
    }

    @Test
    void shouldThrowExceptionIfVideoNotFoundById() {
        whenVideoRepositoryFindByIdThenReturn(Optional.empty());

        assertThrows(EntityObjectNotFoundException.class,
                () -> videoService.getVideoBasicData(1L),
                "Video not found.");
    }

    @Test
    void shouldGetVideosBasicData() {
        final int EXPECTED_SIZE = 10;

        whenVideoRepositoryFindAllBySpecificationTheReturn(EXPECTED_SIZE);

        assertThat(videoService.getVideosBasicData("asd")).hasSize(EXPECTED_SIZE);
    }

    @Test
    void shouldGetVideoDTOById() throws EntityObjectNotFoundException {
        whenVideoRepositoryFindByIdThenReturn(Optional.of(Mocks.videoWithEmptyCollectionsMock()));

        assertThat(videoService.getVideoDTOById(1L)).isNotNull();
    }

    @Test
    void shouldAddGenre() throws EntityObjectNotFoundException {
        Video video = Mocks.videoWithEmptyCollectionsMock();
        Genre genre = Mocks.genreMock();

        whenVideoRepositorySaveThenAnswer();
        whenVideoRepositoryFindByIdThenReturn(Optional.of(video));
        whenGenreServiceFindByIdThenReturn(genre);

        videoService.addGenre(1L, 2L);
        
        verify(videoRepository, times(1)).save(
                Mockito.argThat(arg -> arg.getGenres().contains(genre) == true));
    }

    @Test
    void shouldRemoveGenre() throws EntityObjectNotFoundException {
        Video video = Mocks.videoWithEmptyCollectionsMock();
        Genre genre = Mocks.genreMock();
        video.addGenre(genre);

        whenVideoRepositorySaveThenAnswer();
        whenVideoRepositoryFindByIdThenReturn(Optional.of(video));
        whenGenreServiceFindByIdThenReturn(genre);

        videoService.removeGenre(1L, 2L);

        verify(videoRepository, times(1)).save(
                Mockito.argThat(arg -> arg.getGenres().contains(genre) == false));
    }

    @Test
    void shouldAddCastMember() throws EntityObjectNotFoundException {
        Video video = Mocks.videoWithEmptyCollectionsMock();
        Person person = Mocks.personMock();

        whenVideoRepositorySaveThenAnswer();
        whenVideoRepositoryFindByIdThenReturn(Optional.of(video));
        whenPersonServiceFindByIdThenReturn(person);

        videoService.addCastMember(1L, 2L);

        verify(videoRepository, times(1)).save(
                Mockito.argThat(arg -> arg.getCast().contains(person) == true));
    }

    @Test
    void shouldRemoveCastMember() throws EntityObjectNotFoundException {
        Video video = Mocks.videoWithEmptyCollectionsMock();
        Person person = Mocks.personMock();
        video.addCastMember(person);

        whenVideoRepositorySaveThenAnswer();
        whenVideoRepositoryFindByIdThenReturn(Optional.of(video));
        whenPersonServiceFindByIdThenReturn(person);

        videoService.removeCastMember(1L, 2L);

        verify(videoRepository, times(1)).save(
                Mockito.argThat(arg -> arg.getCast().contains(person) == false));
    }

    @Test
    void shouldAddDirector() throws EntityObjectNotFoundException {
        Video video = Mocks.videoWithEmptyCollectionsMock();
        Person person = Mocks.personMock();

        whenVideoRepositorySaveThenAnswer();
        whenVideoRepositoryFindByIdThenReturn(Optional.of(video));
        whenPersonServiceFindByIdThenReturn(person);

        videoService.addDirector(1L, 2L);

        verify(videoRepository, times(1)).save(
                Mockito.argThat(arg -> arg.getDirectors().contains(person) == true));
    }

    @Test
    void shouldRemoveDirector() throws EntityObjectNotFoundException {
        Video video = Mocks.videoWithEmptyCollectionsMock();
        Person person = Mocks.personMock();
        video.addDirector(person);

        whenVideoRepositorySaveThenAnswer();
        whenVideoRepositoryFindByIdThenReturn(Optional.of(video));
        whenPersonServiceFindByIdThenReturn(person);

        videoService.removeDirector(1L, 2L);

        verify(videoRepository, times(1)).save(
                Mockito.argThat(arg -> arg.getCast().contains(person) == false));
    }

    @Test
    void shouldAddScreenwriter() throws EntityObjectNotFoundException {
        Video video = Mocks.videoWithEmptyCollectionsMock();
        Person person = Mocks.personMock();

        whenVideoRepositorySaveThenAnswer();
        whenVideoRepositoryFindByIdThenReturn(Optional.of(video));
        whenPersonServiceFindByIdThenReturn(person);

        videoService.addScreenwriter(1L, 2L);

        verify(videoRepository, times(1)).save(
                Mockito.argThat(arg -> arg.getScreenwriters().contains(person) == true));
    }

    @Test
    void shouldRemoveScreenwriter() throws EntityObjectNotFoundException {
        Video video = Mocks.videoWithEmptyCollectionsMock();
        Person person = Mocks.personMock();
        video.addScreenwriter(person);

        whenVideoRepositorySaveThenAnswer();
        whenVideoRepositoryFindByIdThenReturn(Optional.of(video));
        whenPersonServiceFindByIdThenReturn(person);

        videoService.removeScreenwriter(1L, 2L);

        verify(videoRepository, times(1)).save(
                Mockito.argThat(arg -> arg.getScreenwriters().contains(person) == false));
    }

    @Test
    void shouldGetVideoPath() throws EntityObjectNotFoundException {
        whenVideoRepositoryFindByIdThenReturn(Optional.of(Mocks.videoWithEmptyCollectionsMock()));

        assertThat(videoService.getVideoPath(1L).getPath()).isNotNull();
    }

    @Test
    void shouldGetVideoMiniaturePath() throws EntityObjectNotFoundException {
        whenVideoRepositoryFindByIdThenReturn(Optional.of(Mocks.videoWithEmptyCollectionsMock()));

        assertThat(videoService.getVideoMiniaturePath(1L)).isNotNull();
    }
}
