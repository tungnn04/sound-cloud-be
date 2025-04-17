package com.example.soundcloudbe.service;

import com.example.soundcloudbe.entity.History;
import com.example.soundcloudbe.entity.User;
import com.example.soundcloudbe.exception.AppException;
import com.example.soundcloudbe.exception.ErrorCode;
import com.example.soundcloudbe.model.request.HistoryRequest;
import com.example.soundcloudbe.repository.HistoryRepository;
import com.example.soundcloudbe.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HistoryService {
    @Autowired
    private HistoryRepository historyRepository;
    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void saveHistory(HistoryRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));
        History history = new History();
        history.setUserId(user.getId());
        history.setSongId(request.getSongId());
        history.setListenedAt(new Date());

        historyRepository.save(history);
    }

    public Page<History> getAllHistory(Pageable pageable) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email).orElseThrow(()
                -> new AppException(ErrorCode.RESOURCE_NOT_EXISTED));

        return historyRepository.findByUserId(user.getId(), pageable);
    }
}
