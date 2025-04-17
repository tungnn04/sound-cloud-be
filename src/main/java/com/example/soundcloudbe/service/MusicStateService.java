package com.example.soundcloudbe.service;

import com.example.soundcloudbe.entity.MusicState;
import com.example.soundcloudbe.entity.User;
import com.example.soundcloudbe.exception.AppException;
import com.example.soundcloudbe.exception.ErrorCode;
import com.example.soundcloudbe.model.dto.MusicStateDTO;
import com.example.soundcloudbe.repository.MusicStateRepository;
import com.example.soundcloudbe.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MusicStateService {
    @Autowired
    private MusicStateRepository musicStateRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void saveMusicState(MusicStateDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));

        MusicState musicState = new MusicState();
        BeanUtils.copyProperties(dto, musicState);
        musicState.setId(user.getId());

        musicStateRepository.save(musicState);
    }

    public MusicStateDTO getMusicState() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));

        Optional<MusicState> musicState = musicStateRepository.findById(user.getId());

        if (musicState.isEmpty()) {
            return MusicStateDTO.builder()
                    .songId(null)
                    .position(0)
                    .isPlaying(false)
                    .build();
        }
        return MusicStateDTO.builder()
                .songId(musicState.get().getSongId())
                .position(musicState.get().getPosition())
                .isPlaying(musicState.get().getIsPlaying())
                .build();
    }
}
