package com.video.application.video;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.video.application.exceptions.EntityObjectAlreadyExistsException;
import com.video.application.exceptions.EntityObjectNotFoundException;
import com.video.application.genre.GenreService;
import com.video.application.person.PersonService;
import com.video.application.video.dto.AddVideoRequest;
import com.video.application.video.dto.VideoBasicData;
import com.video.application.video.dto.VideoDTO;
import com.video.application.video.dto.VideoPath;
import com.video.domain.Genre;
import com.video.domain.Person;
import com.video.domain.Video;
import com.video.domain.repository.VideoRepository;
import com.video.domain.specification.VideoSpecification;

@Service
public class VideoService {

    private final VideoRepository videoRepository;
    private final PersonService personService;
    private final GenreService genreService;

    @Autowired
    public VideoService(VideoRepository videoRepository, PersonService personService, GenreService genreService) {
        this.videoRepository = videoRepository;
        this.personService = personService;
        this.genreService = genreService;
    }

    public VideoDTO addVideo(AddVideoRequest addVideoRequest)
            throws EntityObjectAlreadyExistsException {
        checkIfVideoAlreadyExists(addVideoRequest);
        Video video = Video.create(
                addVideoRequest.getMiniaturePath(),
                addVideoRequest.getFilePath(),
                addVideoRequest.getTitle(),
                addVideoRequest.getYear(),
                addVideoRequest.getDescription(),
                getGenresByIds(addVideoRequest.getGenresIds()),
                getPersonsByIds(addVideoRequest.getCastIds()),
                getPersonsByIds(addVideoRequest.getDirectorsIds()),
                getPersonsByIds(addVideoRequest.getScreenwritersIds()));
        return saveVideoAndMapToDTO(video);
    }

    public void deleteVideo(Long videoId) throws EntityObjectNotFoundException {
        checkIfVideoExistsById(videoId);
        videoRepository.deleteById(videoId);
    }

    public VideoBasicData getVideoBasicData(Long videoId) throws EntityObjectNotFoundException {
        return mapToVideoBasicData(videoRepository.findById(videoId)
                .orElseThrow(() -> new EntityObjectNotFoundException(Video.class.getSimpleName())));
    }

    public List<VideoBasicData> getVideosBasicData(String titlePhrase) {
        return videoRepository.findAll(VideoSpecification.byTitlePhrase(titlePhrase)).stream()
                .map(video -> mapToVideoBasicData(video)).collect(Collectors.toList());
    }

    public VideoDTO getVideoDTOById(Long id) throws EntityObjectNotFoundException {
        return mapToVideoDTO(getVideoById(id));
    }

    public VideoDTO addGenre(Long videoId, Long genreId) throws EntityObjectNotFoundException {
        Video video = getVideoById(videoId).addGenre(getGenreById(genreId));
        return saveVideoAndMapToDTO(video);
    }

    public VideoDTO removeGenre(Long videoId, Long genreId) throws EntityObjectNotFoundException {
        Video video = getVideoById(videoId).removeGenre(getGenreById(genreId));
        return saveVideoAndMapToDTO(video);
    }

    public VideoDTO addCastMember(Long videoId, Long personId) throws EntityObjectNotFoundException {
        Video video = getVideoById(videoId).addCastMember(getPersonById(personId));
        return saveVideoAndMapToDTO(video);
    }

    public VideoDTO removeCastMember(Long videoId, Long personId) throws EntityObjectNotFoundException {
        Video video = getVideoById(videoId).removeCastMember(getPersonById(personId));
        return saveVideoAndMapToDTO(video);
    }

    public VideoDTO addDirector(Long videoId, Long personId) throws EntityObjectNotFoundException {
        Video video = getVideoById(videoId).addDirector(getPersonById(personId));
        return saveVideoAndMapToDTO(video);
    }

    public VideoDTO removeDirector(Long videoId, Long personId) throws EntityObjectNotFoundException {
        Video video = getVideoById(videoId).removeDirector(getPersonById(personId));
        return saveVideoAndMapToDTO(video);
    }

    public VideoDTO addScreenwriter(Long videoId, Long personId) throws EntityObjectNotFoundException {
        Video video = getVideoById(videoId).addScreenwriter(getPersonById(personId));
        return saveVideoAndMapToDTO(video);
    }

    public VideoDTO removeScreenwriter(Long videoId, Long personId) throws EntityObjectNotFoundException {
        Video video = getVideoById(videoId).removeScreenwriter(getPersonById(personId));
        return saveVideoAndMapToDTO(video);
    }

    public VideoPath getVideoPath(Long videoId) throws EntityObjectNotFoundException {
        return VideoPath.builder()
                .path(getVideoById(videoId).getFilePath())
                .build();
    }

    private void checkIfVideoAlreadyExists(AddVideoRequest addVideoRequest) throws EntityObjectAlreadyExistsException {
        if (videoRepository.existsByFilePath(addVideoRequest.getFilePath())) {
            throw new EntityObjectAlreadyExistsException(Video.class.getSimpleName());
        }
    }

    private void checkIfVideoExistsById(Long videoId) throws EntityObjectNotFoundException {
        if (!videoRepository.existsById(videoId)) {
            throw new EntityObjectNotFoundException(Video.class.getSimpleName());
        }
    }

    private VideoDTO saveVideoAndMapToDTO(Video video) {
        return mapToVideoDTO(videoRepository.save(video));
    }

    private Video getVideoById(Long id) throws EntityObjectNotFoundException {
        return videoRepository.findById(id)
                .orElseThrow(() -> new EntityObjectNotFoundException(Video.class.getSimpleName()));
    }

    private Set<Person> getPersonsByIds(Set<Long> ids) {
        return new HashSet<>(personService.findAllByIdsIn(ids));
    }

    private Set<Genre> getGenresByIds(Set<Long> ids) {
        return new HashSet<>(genreService.findAllByIdsIn(ids));
    }

    private Person getPersonById(Long id) throws EntityObjectNotFoundException {
        return personService.findPersonById(id);
    }

    private Genre getGenreById(Long id) throws EntityObjectNotFoundException {
        return genreService.findGenreById(id);
    }

    private VideoDTO mapToVideoDTO(Video video) {
        return VideoDTO.builder()
                .basicData(mapToVideoBasicData(video))
                .genres(video.getGenres())
                .cast(video.getCast())
                .directors(video.getDirectors())
                .screenwriters(video.getScreenwriters())
                .build();
    }

    private VideoBasicData mapToVideoBasicData(Video video) {
        return VideoBasicData.builder()
                .id(video.getId())
                .title(video.getTitle())
                .year(video.getYear())
                .description(video.getDescription())
                .build();
    }

}
