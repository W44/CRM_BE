package com.crmw.CRM_BE.service;


import com.crmw.CRM_BE.entity.UserHistory;
import com.crmw.CRM_BE.enums.HistoryTypes;
import com.crmw.CRM_BE.repository.IUserHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserHistoryService {

    @Autowired
    private IUserHistoryRepository historyRepository;

    public List<UserHistory> getByUserId(Integer userId) {
        return historyRepository.findByUser_Id(userId);
    }

    public List<UserHistory> getByUsername(String username) {
        return historyRepository.findByUser_Username(username);
    }

    public long countByUserId(Integer userId) {
        return historyRepository.countByUser_Id(userId);
    }

    public long countByType(HistoryTypes type) {
        return historyRepository.countByType(type);
    }

    public long countByUserIdAndType(Integer userId, HistoryTypes type) {
        return historyRepository.countByUser_IdAndType(userId, type);
    }
}
