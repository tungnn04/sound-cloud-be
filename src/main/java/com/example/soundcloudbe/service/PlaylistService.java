package com.example.soundcloudbe.service;

import com.example.soundcloudbe.entity.Playlist;
import com.example.soundcloudbe.entity.PlaylistSong;
import com.example.soundcloudbe.entity.Song;
import com.example.soundcloudbe.entity.User;
import com.example.soundcloudbe.exception.AppException;
import com.example.soundcloudbe.exception.ErrorCode;
import com.example.soundcloudbe.model.dto.PlaylistDTO;
import com.example.soundcloudbe.model.dto.SongResponse;
import com.example.soundcloudbe.model.request.PlaylistRequest;
import com.example.soundcloudbe.repository.PlaylistRepository;
import com.example.soundcloudbe.repository.PlaylistSongRepository;
import com.example.soundcloudbe.repository.SongRepository;
import com.example.soundcloudbe.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PlaylistService {
    @Autowired
    private PlaylistRepository playlistRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlaylistSongRepository playlistSongRepository;
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private PlaylistSongService playlistSongService;
    @Autowired
    private CloudinaryService cloudinaryService;

    public List<PlaylistDTO> findAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));

        List<Playlist> playlists = playlistRepository.findByUserId(user.getId());
        List<PlaylistDTO> playlistDTOS = new ArrayList<>();
        if (playlists != null) {
            playlists.forEach(playlist -> {
                PlaylistDTO playlistDTO = new PlaylistDTO();
                BeanUtils.copyProperties(playlist, playlistDTO);
                List<SongResponse> songs = playlistSongService.findSongByPlaylistId(playlist.getId());
                playlistDTO.setSongs(songs);
                playlistDTOS.add(playlistDTO);
            });
        }
        return playlistDTOS;
    }

    public void createPlaylist(PlaylistRequest dto) {
        Playlist playlist = playlistRepository.findByName(dto.getName());
        if (playlist != null) {
            throw new AppException(ErrorCode.RESOURCE_EXISTED);
        }
        Playlist playlistEntity = new Playlist();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));
        playlistEntity.setName(dto.getName());
        playlistEntity.setUserId(user.getId());
        playlistEntity.setCoverUrl("https://res.cloudinary.com/dcwopmt83/image/upload/v1743407599/playlist_default_no973y.jpg");
        playlistEntity.setCreatedAt(new Date());

        playlistRepository.save(playlistEntity);
    }

    public void updatePlaylist(Integer id, PlaylistRequest dto) throws IOException {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));
        cloudinaryService.deleteFile(playlist.getCoverUrl());
        playlist.setName(dto.getName());
        String url = cloudinaryService.uploadFile(dto.getCoverImage(), "playlist", true);
        playlist.setCoverUrl(url);

        playlistRepository.save(playlist);
    }

    public PlaylistDTO detail(Integer id) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));
        PlaylistDTO playlistDTO = new PlaylistDTO();
        BeanUtils.copyProperties(playlist, playlistDTO);

        List<SongResponse> songs = playlistSongService.findSongByPlaylistId(playlist.getId());
        playlistDTO.setSongs(songs);

        return playlistDTO;
    }

    @Transactional
    public void delete(Integer id) {
        Playlist playlist = playlistRepository.findById(id).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));
        playlistRepository.delete(playlist);

        List<PlaylistSong> playlistSongs = playlistSongRepository.findByPlaylistId(playlist.getId());
        if (playlistSongs != null) {
            playlistSongRepository.deleteAll(playlistSongs);
        }
        cloudinaryService.deleteFile(playlist.getCoverUrl());
    }

    public void addSong(Integer playlistId, Integer songId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));
        Song song = songRepository.findById(songId).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));
        PlaylistSong playlistSong = playlistSongRepository.findByPlaylistIdAndSongId(playlistId, songId);
        if (playlistSong != null) {
            throw new AppException(ErrorCode.RESOURCE_EXISTED);
        }

        PlaylistSong entity = new PlaylistSong();
        entity.setPlaylistId(playlist.getId());
        entity.setSongId(song.getId());

        playlistSongRepository.save(entity);
    }

    public void deleteSong(Integer playlistId, Integer songId) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));
        Song song = songRepository.findById(songId).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));

        PlaylistSong playlistSong = playlistSongRepository.findByPlaylistIdAndSongId(playlistId, songId);
        if (playlistSong == null) {
            throw new AppException(ErrorCode.RESOURCE_NOT_EXISTED);
        }
        playlistSongRepository.delete(playlistSong);
    }
}
