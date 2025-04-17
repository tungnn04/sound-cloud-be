package com.example.soundcloudbe.service;

import lombok.extern.slf4j.Slf4j;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@Service
@Slf4j
public class AudioMetadataService {
    public Integer getAudioDuration(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return 0;
        }

        File tempFile = null;
        try {
            tempFile = Files.createTempFile("temp_audio_", "_" + file.getOriginalFilename()).toFile();
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(file.getBytes());
            }

            AudioFile audioFile = AudioFileIO.read(tempFile);
            int duration = audioFile.getAudioHeader().getTrackLength();

            if (duration > 0) {
                return duration;
            }
            else {
                log.warn("Could not determine valid duration for file: {}", file.getOriginalFilename());
                return 0;
            }
        } catch (IOException e) {
            log.error("IO Error creating/reading temp file for audio duration: {}", file.getOriginalFilename(), e);
            return 0;
        } catch (CannotReadException | TagException | InvalidAudioFrameException | ReadOnlyFileException e ) {
            log.error("JAudioTagger Error reading audio duration for file: {}: {}", file.getOriginalFilename(), e.getMessage());
            return 0;
        } catch (Exception e) {
            log.error("Unexpected Error reading audio duration for file: {}", file.getOriginalFilename(), e);
            return 0;
        } finally {
            if (tempFile != null && tempFile.exists()) {
                if (!tempFile.delete()) {
                    log.warn("Could not delete temporary audio file: {}", tempFile.getAbsolutePath());
                }
            }
        }
    }
}
