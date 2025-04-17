package com.example.soundcloudbe.service;

import com.example.soundcloudbe.model.dto.AlbumResponse;
import com.example.soundcloudbe.model.request.SearchAlbumRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AlbumService {
    Page<AlbumResponse> findAll(Pageable pageable, SearchAlbumRequest request);

    AlbumResponse detail(Integer id);
}
