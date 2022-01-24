package com.video.application.video;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.video.application.exceptions.EntityObjectNotFoundException;
import com.video.application.exceptions.FileException;
import com.video.application.video.VideoFileService;
import com.video.application.video.VideoService;

@ExtendWith(MockitoExtension.class)
public class VideoFileServiceTest {

    @InjectMocks
    private VideoFileService videoFileService;

    @Mock
    private VideoService videoService;

    private static final String TEST_FILE_PATH = "src/test/resources/testFile.jpg";
    private static final int TEST_FILE_SIZE_BYTES = 5133;

    @Test
    void shouldGetVideoMiniatureFile() throws EntityObjectNotFoundException, FileException {
        when(videoService.getVideoMiniaturePath(Mockito.anyLong()))
                .thenReturn(TEST_FILE_PATH);

        assertThat(videoFileService.getVideoMiniatureFile(1L)).hasSize(TEST_FILE_SIZE_BYTES);
    }
}
