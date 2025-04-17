package com.example.soundcloudbe.service.impl;

import com.example.soundcloudbe.entity.Song;
import com.example.soundcloudbe.entity.User;
import com.example.soundcloudbe.exception.AppException;
import com.example.soundcloudbe.exception.ErrorCode;
import com.example.soundcloudbe.model.dto.SongResponse;
import com.example.soundcloudbe.model.request.CreateSongRequest;
import com.example.soundcloudbe.model.request.SearchSongRequest;
import com.example.soundcloudbe.repository.SongRepository;
import com.example.soundcloudbe.repository.UserRepository;
import com.example.soundcloudbe.service.AudioMetadataService;
import com.example.soundcloudbe.service.CloudinaryService;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class SongServiceImpl implements SongService {
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AudioMetadataService audioMetadataService;
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<SongResponse> findAll(SearchSongRequest request, Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));

        StringBuilder str = new StringBuilder();
        HashMap<String, Object> map = new HashMap<>();

        str.append("SELECT s.ID, s.TITLE, ar.NAME AS ARTIST, al.TITLE AS ALBUM, c.NAME AS CATEGORY, s.DURATION, s.FILE_URL, s.COVER_URL, s.PLAY_COUNT, CASE WHEN f.SONG_ID IS NOT NULL THEN 'true' ELSE 'false' END AS IS_FAVORITE " +
                "   FROM SONGS s " +
                "       LEFT JOIN ARTISTS ar on s.ARTIST_ID = ar.ID" +
                "       LEFT JOIN ALBUMS al ON s.ALBUM_ID = al.ID" +
                "       LEFT JOIN CATEGORIES c ON s.CATEGORY_ID = c.ID" +
                "       LEFT JOIN (" +
                "        SELECT * FROM FAVORITES WHERE USER_ID = :userId " +
                "       ) f ON s.ID = f.SONG_ID " +
                "   WHERE 1 = 1");
        map.put("userId", user.getId());

        if (!DataUtil.isNullOrZero(request.getTitle())) {
            str.append(" AND s.TITLE LIKE '%").append(request.getTitle()).append("%'");
        }
        if (!DataUtil.isNullOrZero(request.getCategoryId())) {
            str.append(" AND category_ID = :categoryId");
            map.put("categoryId", request.getCategoryId());
        }

        str.append(" ORDER BY s.CREATED_AT DESC, s.PLAY_COUNT DESC ");

        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) FROM (" + str + ") temp");
        DataConvertUtil.setParams(countQuery, map);

        long totalElements = ((Number) countQuery.getSingleResult()).longValue();

        Query query = entityManager.createNativeQuery(str.toString());
        DataConvertUtil.setParams(query, map);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Object[]> list = query.getResultList();
        List<SongResponse> result = new ArrayList<>();

        if (list != null && !list.isEmpty()){
            list.forEach(e -> {
                SongResponse song = new SongResponse();
                song.setId(DataConvertUtil.safeToInteger(e[0]));
                song.setTitle(DataConvertUtil.safeToString(e[1]));
                song.setArtistName(DataConvertUtil.safeToString(e[2]));
                song.setAlbumName(DataConvertUtil.safeToString(e[3]));
                song.setCategoryName(DataConvertUtil.safeToString(e[4]));
                song.setDuration(DataConvertUtil.safeToInteger(e[5]));
                song.setFileUrl(DataConvertUtil.safeToString(e[6]));
                song.setCoverUrl(DataConvertUtil.safeToString(e[7]));
                song.setPlayCount(DataConvertUtil.safeToInteger(e[8]));
                song.setIsFavorite(DataConvertUtil.safeToBoolean(e[9]));

                result.add(song);
            });
        }
        return new PageImpl<>(result, pageable, totalElements);
    }

    @Override
    public SongResponse getSong(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));

        StringBuilder str = new StringBuilder();
        str.append("SELECT s.ID, s.TITLE, ar.NAME AS ARTIST, al.TITLE AS ALBUM, c.NAME AS CATEGORY, s.DURATION, s.FILE_URL, s.COVER_URL, s.PLAY_COUNT, CASE WHEN f.SONG_ID IS NOT NULL THEN 'true' ELSE 'false' END AS IS_FAVORITE " +
                "   FROM SONGS s " +
                "       LEFT JOIN ARTISTS ar on s.ARTIST_ID = ar.ID" +
                "       LEFT JOIN ALBUMS al ON s.ALBUM_ID = al.ID" +
                "       LEFT JOIN CATEGORIES c ON s.CATEGORY_ID = c.ID" +
                "       LEFT JOIN (" +
                "        SELECT * FROM FAVORITES WHERE USER_ID = :userId " +
                "       ) f ON s.ID = f.SONG_ID " +
                "   WHERE s.ID = :id ");

        Query query = entityManager.createNativeQuery(str.toString());
        query.setParameter("id", id);
        query.setParameter("userId", user.getId());
        Object[] obj = (Object[]) query.getSingleResult();

        SongResponse song = new SongResponse();
        song.setId(DataConvertUtil.safeToInteger(obj[0]));
        song.setTitle(DataConvertUtil.safeToString(obj[1]));
        song.setArtistName(DataConvertUtil.safeToString(obj[2]));
        song.setAlbumName(DataConvertUtil.safeToString(obj[3]));
        song.setCategoryName(DataConvertUtil.safeToString(obj[4]));
        song.setDuration(DataConvertUtil.safeToInteger(obj[5]));
        song.setFileUrl(DataConvertUtil.safeToString(obj[6]));
        song.setCoverUrl(DataConvertUtil.safeToString(obj[7]));
        song.setPlayCount(DataConvertUtil.safeToInteger(obj[8]));
        song.setIsFavorite(DataConvertUtil.safeToBoolean(obj[9]));

        return song;
    }

    @Override
    public void delete(Integer id){
        Song song = songRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));
        if (song.getFileUrl() != null && !song.getFileUrl().isEmpty()){
            cloudinaryService.deleteFile(song.getCoverUrl());
        }
        if ( song.getFileUrl() != null && !song.getFileUrl().isEmpty()){
            cloudinaryService.deleteFile(song.getFileUrl());
        }
        songRepository.delete(song);
    }

    @Override
    public void create(CreateSongRequest request) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));
        String fileUrl = cloudinaryService.uploadFile(request.getAudio(), "audio", false);
        String coverUrl = cloudinaryService.uploadFile(request.getCoverImage(), "song", true);
        Integer duration = audioMetadataService.getAudioDuration(request.getAudio());
        Song song = new Song();
        BeanUtils.copyProperties(request, song);
        song.setFileUrl(fileUrl);
        song.setCoverUrl(coverUrl);
        song.setUploadedBy(user.getId());
        song.setPlayCount(0);
        song.setDuration(duration);
        song.setCreatedAt(new Date());

        songRepository.save(song);
    }

    @Override
    public List<SongResponse> searchByAlbumId(Integer albumId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));

        StringBuilder str = new StringBuilder();

        str.append("SELECT s.ID, s.TITLE, ar.NAME AS ARTIST, al.TITLE AS ALBUM, c.NAME AS CATEGORY, s.DURATION, s.FILE_URL, s.COVER_URL, s.PLAY_COUNT, CASE WHEN f.SONG_ID IS NOT NULL THEN 'true' ELSE 'false' END AS IS_FAVORITE " +
                "   FROM SONGS s " +
                "       INNER JOIN ARTISTS ar on s.ARTIST_ID = ar.ID" +
                "       INNER JOIN ALBUMS al ON s.ALBUM_ID = al.ID" +
                "       INNER JOIN CATEGORIES c ON s.CATEGORY_ID = c.ID" +
                "       LEFT JOIN (" +
                "        SELECT * FROM FAVORITES WHERE USER_ID = :userId " +
                "       ) f ON s.ID = f.SONG_ID " +
                "   WHERE s.ALBUM_ID = :albumId ");

        str.append(" ORDER BY s.CREATED_AT DESC, s.PLAY_COUNT DESC ");
        Query query = entityManager.createNativeQuery(str.toString());
        query.setParameter("albumId", albumId);
        query.setParameter("userId", user.getId());
        List<Object[]> list = query.getResultList();
        List<SongResponse> result = new ArrayList<>();

        if (list != null && !list.isEmpty()){
            list.forEach(e -> {
                SongResponse song = new SongResponse();
                song.setId(DataConvertUtil.safeToInteger(e[0]));
                song.setTitle(DataConvertUtil.safeToString(e[1]));
                song.setArtistName(DataConvertUtil.safeToString(e[2]));
                song.setAlbumName(DataConvertUtil.safeToString(e[3]));
                song.setCategoryName(DataConvertUtil.safeToString(e[4]));
                song.setDuration(DataConvertUtil.safeToInteger(e[5]));
                song.setFileUrl(DataConvertUtil.safeToString(e[6]));
                song.setCoverUrl(DataConvertUtil.safeToString(e[7]));
                song.setPlayCount(DataConvertUtil.safeToInteger(e[8]));
                song.setIsFavorite(DataConvertUtil.safeToBoolean(e[9]));

                result.add(song);
            });
        }
        return result;
    }

    @Override
    public List<SongResponse> searchByArtistId(Integer artistId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));

        StringBuilder str = new StringBuilder();
        str.append("SELECT s.ID, s.TITLE, ar.NAME AS ARTIST, al.TITLE AS ALBUM, c.NAME AS CATEGORY, s.DURATION, s.FILE_URL, s.COVER_URL, s.PLAY_COUNT, CASE WHEN f.SONG_ID IS NOT NULL THEN 'true' ELSE 'false' END AS IS_FAVORITE " +
                "   FROM SONGS s " +
                "       INNER JOIN ARTISTS ar on s.ARTIST_ID = ar.ID" +
                "       LEFT JOIN ALBUMS al ON s.ALBUM_ID = al.ID" +
                "       INNER JOIN CATEGORIES c ON s.CATEGORY_ID = c.ID" +
                "       LEFT JOIN (" +
                "        SELECT * FROM FAVORITES WHERE USER_ID = :userId " +
                "       ) f ON s.ID = f.SONG_ID " +
                "   WHERE s.ARTIST_ID = :artistId ");

        str.append(" ORDER BY s.CREATED_AT DESC, s.PLAY_COUNT DESC ");
        Query query = entityManager.createNativeQuery(str.toString());
        query.setParameter("artistId", artistId);
        query.setParameter("userId", user.getId());
        List<Object[]> list = query.getResultList();
        List<SongResponse> result = new ArrayList<>();

        if (list != null && !list.isEmpty()){
            list.forEach(e -> {
                SongResponse song = new SongResponse();
                song.setId(DataConvertUtil.safeToInteger(e[0]));
                song.setTitle(DataConvertUtil.safeToString(e[1]));
                song.setArtistName(DataConvertUtil.safeToString(e[2]));
                song.setAlbumName(DataConvertUtil.safeToString(e[3]));
                song.setCategoryName(DataConvertUtil.safeToString(e[4]));
                song.setDuration(DataConvertUtil.safeToInteger(e[5]));
                song.setFileUrl(DataConvertUtil.safeToString(e[6]));
                song.setCoverUrl(DataConvertUtil.safeToString(e[7]));
                song.setPlayCount(DataConvertUtil.safeToInteger(e[8]));
                song.setIsFavorite(DataConvertUtil.safeToBoolean(e[9]));

                result.add(song);
            });
        }
        return result;
    }

    @Override
    public List<SongResponse> searchByUploadedBy(Integer userId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));

        StringBuilder str = new StringBuilder();
        str.append("SELECT s.ID, s.TITLE, ar.NAME AS ARTIST, al.TITLE AS ALBUM, c.NAME AS CATEGORY, s.DURATION, s.FILE_URL, s.COVER_URL, s.PLAY_COUNT, CASE WHEN f.SONG_ID IS NOT NULL THEN 'true' ELSE 'false' END AS IS_FAVORITE " +
                "   FROM SONGS s " +
                "       INNER JOIN ARTISTS ar on s.ARTIST_ID = ar.ID" +
                "       LEFT JOIN ALBUMS al ON s.ALBUM_ID = al.ID" +
                "       INNER JOIN CATEGORIES c ON s.CATEGORY_ID = c.ID" +
                "       LEFT JOIN (" +
                "        SELECT * FROM FAVORITES WHERE USER_ID = :userId " +
                "       ) f ON s.ID = f.SONG_ID " +
                "   WHERE s.UPLOADED_BY = :uploadedBy ");

        str.append(" ORDER BY s.CREATED_AT DESC, s.PLAY_COUNT DESC ");
        Query query = entityManager.createNativeQuery(str.toString());
        query.setParameter("uploadedBy", userId);
        query.setParameter("userId", userId);
        List<Object[]> list = query.getResultList();
        List<SongResponse> result = new ArrayList<>();

        if (list != null && !list.isEmpty()){
            list.forEach(e -> {
                SongResponse song = new SongResponse();
                song.setId(DataConvertUtil.safeToInteger(e[0]));
                song.setTitle(DataConvertUtil.safeToString(e[1]));
                song.setArtistName(DataConvertUtil.safeToString(e[2]));
                song.setAlbumName(DataConvertUtil.safeToString(e[3]));
                song.setCategoryName(DataConvertUtil.safeToString(e[4]));
                song.setDuration(DataConvertUtil.safeToInteger(e[5]));
                song.setFileUrl(DataConvertUtil.safeToString(e[6]));
                song.setCoverUrl(DataConvertUtil.safeToString(e[7]));
                song.setPlayCount(DataConvertUtil.safeToInteger(e[8]));
                song.setIsFavorite(DataConvertUtil.safeToBoolean(e[9]));

                result.add(song);
            });
        }
        return result;
    }
}
