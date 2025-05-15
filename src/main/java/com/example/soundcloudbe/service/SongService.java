package com.example.soundcloudbe.service;

import com.example.soundcloudbe.model.dto.SongResponse;
import com.example.soundcloudbe.model.request.CreateSongRequest;
import com.example.soundcloudbe.model.request.SearchSongRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;

public interface SongService {
    Page<SongResponse> findAll(SearchSongRequest request, Pageable pageable);

    SongResponse getSong(Integer id);

    void delete(Integer id);

    void create(CreateSongRequest request) throws IOException;

    List<SongResponse> searchByAlbumId(Integer albumId);

    List<SongResponse> searchByArtistId(Integer artistId);

    List<SongResponse> searchByUploadedBy(Integer userId);

    List<SongResponse> getRelated(Integer id);
}
