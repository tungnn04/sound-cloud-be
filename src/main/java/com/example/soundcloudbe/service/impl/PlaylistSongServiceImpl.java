package com.example.soundcloudbe.service.impl;

import com.example.soundcloudbe.entity.User;
import com.example.soundcloudbe.exception.AppException;
import com.example.soundcloudbe.exception.ErrorCode;
import com.example.soundcloudbe.model.dto.SongResponse;
import com.example.soundcloudbe.repository.UserRepository;
import com.example.soundcloudbe.service.PlaylistSongService;
import com.example.soundcloudbe.util.DataConvertUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PlaylistSongServiceImpl implements PlaylistSongService {
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<SongResponse> findSongByPlaylistId(Integer playlistId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));
        StringBuilder str = new StringBuilder();
        str.append(" SELECT s.ID, s.TITLE, ar.NAME AS ARTIST, al.TITLE AS ALBUM, c.NAME AS CATEGORY, s.DURATION, s.FILE_URL, s.COVER_URL, s.PLAY_COUNT, CASE WHEN f.SONG_ID IS NOT NULL THEN true ELSE false END AS IS_FAVORITE " +
                "    FROM PLAYLIST_SONGS ps " +
                "       INNER JOIN SONGS s ON ps.SONG_ID = s.ID" +
                "       LEFT JOIN ARTISTS ar on s.ARTIST_ID = ar.ID " +
                "       LEFT JOIN ALBUMS al ON s.ALBUM_ID = al.ID" +
                "       LEFT JOIN CATEGORIES c ON s.CATEGORY_ID = c.ID " +
                "       LEFT JOIN (" +
                "        SELECT * FROM FAVORITES WHERE USER_ID = :userId " +
                "       ) f ON s.ID = f.SONG_ID " +
                "    WHERE ps.PLAYLIST_ID = :playlistId ");

        Query query = entityManager.createNativeQuery(str.toString());
        query.setParameter("playlistId", playlistId);
        query.setParameter("userId", user.getId());

        List<Object[]> list = query.getResultList();
        List<SongResponse> songResponseList = new ArrayList<>();
        if (list != null && !list.isEmpty()) {
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

                songResponseList.add(song);
            });
        }
        return songResponseList;
    }


}
