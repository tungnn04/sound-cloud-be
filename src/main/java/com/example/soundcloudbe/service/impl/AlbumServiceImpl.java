package com.example.soundcloudbe.service.impl;

import com.example.soundcloudbe.model.dto.AlbumResponse;
import com.example.soundcloudbe.model.request.SearchAlbumRequest;
import com.example.soundcloudbe.service.AlbumService;
import com.example.soundcloudbe.service.SongService;
import com.example.soundcloudbe.util.DataConvertUtil;
import com.example.soundcloudbe.util.DataUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AlbumServiceImpl implements AlbumService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private SongService songService;

    @Override
    public Page<AlbumResponse> findAll(Pageable pageable, SearchAlbumRequest request) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT AL.ID, AL.TITLE, AR.NAME, AL.COVER_URL, CAST(YEAR(AL.RELEASE_DATE) AS UNSIGNED) AS YEAR FROM ALBUMS AL" +
                "   INNER JOIN ARTISTS AR ON AL.ARTIST_ID = AR.ID ");

        if (!DataUtil.isNullOrZero(request.getTitle())){
            str.append(" AND AL.TITLE LIKE '%").append(request.getTitle()).append("%'");
        }
        str.append(" ORDER BY AL.CREATED_AT DESC ");
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) FROM (" + str + ") temp");
        long totalElements = ((Number) countQuery.getSingleResult()).longValue();

        Query query = entityManager.createNativeQuery(str.toString());
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> lst = query.getResultList();
        List<AlbumResponse> res = new ArrayList<>();

        if (lst != null && !lst.isEmpty()) {
            lst.forEach(e -> {
                AlbumResponse albumResponse = new AlbumResponse();
                albumResponse.setId(DataConvertUtil.safeToInteger(e[0]));
                albumResponse.setTitle(DataConvertUtil.safeToString(e[1]));
                albumResponse.setArtistName(DataConvertUtil.safeToString(e[2]));
                albumResponse.setCoverUrl(DataConvertUtil.safeToString(e[3]));
                albumResponse.setReleaseYear(DataConvertUtil.safeToInteger(e[4]));

                albumResponse.setSongs(songService.searchByAlbumId(albumResponse.getId()));
                res.add(albumResponse);
            });
        }

        return new PageImpl<>(res, pageable, totalElements);
    }

    @Override
    public AlbumResponse detail(Integer id) {
        StringBuilder str = new StringBuilder();
        str.append("SELECT AL.ID, AL.TITLE, AR.NAME, AL.COVER_URL, CAST(YEAR(AL.RELEASE_DATE) AS UNSIGNED) AS YEAR " +
                "   FROM ALBUMS AL" +
                "       INNER JOIN ARTISTS AR ON AL.ARTIST_ID = AR.ID" +
                "   WHERE AL.ID = :id ");

        Query query = entityManager.createNativeQuery(str.toString());
        query.setParameter("id", id);
        Object[] obj =(Object[]) query.getSingleResult();

        AlbumResponse albumResponse = new AlbumResponse();
        albumResponse.setId(DataConvertUtil.safeToInteger(obj[0]));
        albumResponse.setTitle(DataConvertUtil.safeToString(obj[1]));
        albumResponse.setArtistName(DataConvertUtil.safeToString(obj[2]));
        albumResponse.setCoverUrl(DataConvertUtil.safeToString(obj[3]));
        albumResponse.setReleaseYear(DataConvertUtil.safeToInteger(obj[4]));

        albumResponse.setSongs(songService.searchByAlbumId(id));
        return albumResponse;
    }
}
