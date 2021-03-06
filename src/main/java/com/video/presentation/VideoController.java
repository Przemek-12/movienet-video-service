package com.video.presentation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.video.application.exceptions.EntityObjectAlreadyExistsException;
import com.video.application.exceptions.EntityObjectNotFoundException;
import com.video.application.exceptions.FileException;
import com.video.application.video.VideoFileService;
import com.video.application.video.VideoService;
import com.video.application.video.dto.AddVideoRequest;
import com.video.application.video.dto.VideoBasicData;
import com.video.application.video.dto.VideoDTO;
import com.video.application.video.dto.VideoPath;
import com.video.infrastructure.security.SecurityUtils;

@RequestMapping("/video")
@RestController
public class VideoController {

    private final VideoService videoService;
    private final VideoFileService videoFileService;

    @Autowired
    public VideoController(VideoService videoService, VideoFileService videoFileService) {
        this.videoService = videoService;
        this.videoFileService = videoFileService;
    }

    @PostMapping
    @Secured(SecurityUtils.ROLE_ADMIN)
    public VideoDTO addVideo(@RequestBody AddVideoRequest addVideoRequest) {
        try {
            return videoService.addVideo(addVideoRequest);
        } catch (EntityObjectAlreadyExistsException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }
    
    @GetMapping
    public VideoDTO getVideoById(@RequestParam Long videoId) {
        try {
            return videoService.getVideoDTOById(videoId);
        } catch (EntityObjectNotFoundException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @GetMapping("/exists")
    public boolean videoExistsById(@RequestParam Long videoId) {
        return videoService.videoExistsById(videoId);
    }

    @DeleteMapping
    @Secured(SecurityUtils.ROLE_ADMIN)
    public void deleteVideo(@RequestParam Long videoId) {
        try {
            videoService.deleteVideo(videoId);
        } catch (EntityObjectNotFoundException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @GetMapping("/miniature")
    public ResponseEntity<byte[]> getVideoMiniatureFile(@RequestParam Long videoId) {
        try {
            return ResponseEntity.ok(videoFileService.getVideoMiniatureFile(videoId));
        } catch (EntityObjectNotFoundException | FileException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @GetMapping("/path")
    public VideoPath getVideoPath(@RequestParam Long videoId) {
        try {
            return videoService.getVideoPath(videoId);
        } catch (EntityObjectNotFoundException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @GetMapping("/basic-data")
    public VideoBasicData getVideoBasicData(@RequestParam Long videoId) {
        try {
            return videoService.getVideoBasicData(videoId);
        } catch (EntityObjectNotFoundException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @GetMapping("/basic-data/all")
    public List<VideoBasicData> getVideosBasicData(@RequestParam String titlePhrase) {
        return videoService.getVideosBasicData(titlePhrase);
    }

    @PutMapping("/genre/add")
    @Secured(SecurityUtils.ROLE_ADMIN)
    public VideoDTO addGenre(Long videoId, Long genreId) {
        try {
            return videoService.addGenre(videoId, genreId);
        } catch (EntityObjectNotFoundException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @PutMapping("/genre/remove")
    @Secured(SecurityUtils.ROLE_ADMIN)
    public VideoDTO removeGenre(Long videoId, Long genreId) {
        try {
            return videoService.removeGenre(videoId, genreId);
        } catch (EntityObjectNotFoundException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @PutMapping("/cast/add")
    @Secured(SecurityUtils.ROLE_ADMIN)
    public VideoDTO addCastMember(Long videoId, Long personId) {
        try {
            return videoService.addCastMember(videoId, personId);
        } catch (EntityObjectNotFoundException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @PutMapping("/cast/remove")
    @Secured(SecurityUtils.ROLE_ADMIN)
    public VideoDTO removeCastMember(Long videoId, Long personId) {
        try {
            return videoService.removeCastMember(videoId, personId);
        } catch (EntityObjectNotFoundException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @PutMapping("/director/add")
    @Secured(SecurityUtils.ROLE_ADMIN)
    public VideoDTO addDirector(Long videoId, Long personId) {
        try {
            return videoService.addDirector(videoId, personId);
        } catch (EntityObjectNotFoundException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @PutMapping("/director/remove")
    @Secured(SecurityUtils.ROLE_ADMIN)
    public VideoDTO removeDirector(Long videoId, Long personId) {
        try {
            return videoService.removeDirector(videoId, personId);
        } catch (EntityObjectNotFoundException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @PutMapping("/screenwriter/add")
    @Secured(SecurityUtils.ROLE_ADMIN)
    public VideoDTO addScreenwriter(Long videoId, Long personId) {
        try {
            return videoService.addScreenwriter(videoId, personId);
        } catch (EntityObjectNotFoundException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

    @PutMapping("/screenwriter/remove")
    @Secured(SecurityUtils.ROLE_ADMIN)
    public VideoDTO removeScreenwriter(Long videoId, Long personId) {
        try {
            return videoService.removeScreenwriter(videoId, personId);
        } catch (EntityObjectNotFoundException e) {
            throw new ResponseStatusException(e.getStatus(), e.getMessage(), e);
        }
    }

}
