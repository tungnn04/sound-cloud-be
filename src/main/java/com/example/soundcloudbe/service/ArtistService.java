package com.example.soundcloudbe.service;

import com.example.soundcloudbe.model.dto.ArtistResponse;
import com.example.soundcloudbe.model.request.SearchArtistRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ArtistService {
    Page<ArtistResponse> findAll(Pageable pageable, SearchArtistRequest request);

    ArtistResponse detail(Integer id);
}
