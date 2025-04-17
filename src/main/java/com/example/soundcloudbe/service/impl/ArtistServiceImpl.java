package com.example.soundcloudbe.service.impl;

import com.example.soundcloudbe.entity.Album;
import com.example.soundcloudbe.entity.Artist;
import com.example.soundcloudbe.entity.Song;
import com.example.soundcloudbe.exception.AppException;
import com.example.soundcloudbe.exception.ErrorCode;
import com.example.soundcloudbe.model.dto.AlbumResponse;
import com.example.soundcloudbe.model.dto.ArtistResponse;
import com.example.soundcloudbe.model.dto.SongResponse;
import com.example.soundcloudbe.model.request.SearchArtistRequest;
import com.example.soundcloudbe.repository.AlbumRepository;
import com.example.soundcloudbe.repository.ArtistRepository;
import com.example.soundcloudbe.repository.SongRepository;
import com.example.soundcloudbe.service.ArtistService;
import com.example.soundcloudbe.service.SongService;
import com.example.soundcloudbe.util.DataConvertUtil;
import com.example.soundcloudbe.util.DataUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class ArtistServiceImpl implements ArtistService {
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private AlbumRepository albumRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private SongService songService;

    @Override
    public Page<ArtistResponse> findAll(Pageable pageable, SearchArtistRequest request) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT ID, NAME, PROFILE_PICTURE FROM ARTISTS WHERE 1 = 1 ");
        if (!DataUtil.isNullOrZero(request.getName())) {
            str.append(" AND NAME LIKE '%").append(request.getName()).append("%'");
        }
        str.append(" ORDER BY CREATED_AT DESC ");

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) FROM (" + str + ") temp");
        long totalElements = ((Number) countQuery.getSingleResult()).longValue();

        Query query = entityManager.createNativeQuery(str.toString());
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> lst = query.getResultList();
        List<ArtistResponse> artists = new ArrayList<>();

        if (lst != null && !lst.isEmpty()) {
            lst.forEach(e -> {
                ArtistResponse artist = new ArtistResponse();
                artist.setId(DataConvertUtil.safeToInteger(e[0]));
                artist.setName(DataConvertUtil.safeToString(e[1]));
                artist.setProfilePicture(DataConvertUtil.safeToString(e[2]));
                List<Album> albums = albumRepository.findByArtistId(artist.getId());
                List<AlbumResponse> albumResponses = new ArrayList<>();

                if (!albums.isEmpty()) {
                    albums.forEach(album -> {
                        AlbumResponse albumResponse = new AlbumResponse();
                        BeanUtils.copyProperties(album, albumResponse);
                        albumResponse.setArtistName(artist.getName());
                        albumResponse.setReleaseYear((album.getReleaseDate().getYear()));
                        albumResponses.add(albumResponse);
                    });
                }
                artist.setAlbums(albumResponses);

                artist.setSongs(songService.searchByArtistId(artist.getId()));
                artists.add(artist);
            });
        }

        return new PageImpl<>(artists, pageable, totalElements);
    }

    @Override
    public ArtistResponse detail(Integer id) {
        Artist artist = artistRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));
        ArtistResponse artistResponse = new ArtistResponse();
        BeanUtils.copyProperties(artist, artistResponse);

        List<Album> albums = albumRepository.findByArtistId(artistResponse.getId());
        List<AlbumResponse> albumResponses = new ArrayList<>();

        if (!albums.isEmpty()) {
            albums.forEach(e -> {
                AlbumResponse albumResponse = new AlbumResponse();
                BeanUtils.copyProperties(e, albumResponse);
                albumResponse.setArtistName(artistResponse.getName());
                albumResponse.setReleaseYear((e.getReleaseDate().getYear()));
                albumResponses.add(albumResponse);
            });
        }
        artistResponse.setAlbums(albumResponses);

        artistResponse.setSongs(songService.searchByArtistId(id));

        return artistResponse;
    }
}
