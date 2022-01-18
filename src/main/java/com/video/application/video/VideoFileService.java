package com.video.application.video;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.video.application.exceptions.EntityObjectNotFoundException;
import com.video.application.exceptions.FileException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class VideoFileService {

    private final VideoService videoService;

    @Autowired
    public VideoFileService(VideoService videoService) {
        this.videoService = videoService;
    }

    public byte[] getVideoMiniatureFile(Long videoId) throws EntityObjectNotFoundException, FileException {
        String filePath = videoService.getVideoMiniaturePath(videoId);
        try {
            return Files.readAllBytes(Paths.get(filePath));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileException();
        }
    }

}
