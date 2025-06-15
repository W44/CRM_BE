package com.crmw.CRM_BE.service;


import com.crmw.CRM_BE.entity.UserHistory;
import com.crmw.CRM_BE.entity.Users;
import com.crmw.CRM_BE.enums.HistoryTypes;
import com.crmw.CRM_BE.repository.IUserHistoryRepository;
import com.crmw.CRM_BE.repository.IUsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserHistoryService {

    @Autowired
    private IUserHistoryRepository historyRepository;

    @Autowired
    IUsersRepository iUsersRepository;

    public Users getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return iUsersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public UserHistory saveUserHistory(UserHistory userHistory)
    {
        Users currentUser = getCurrentUser();
        userHistory.setUser(currentUser);
        return historyRepository.save(userHistory);
    }

    public List<UserHistory> getByUserId(Integer userId) {
        return historyRepository.findByUser_Id(userId);
    }

    public List<UserHistory> getByUsername(String username) {
        return historyRepository.findByUser_Username(username);
    }

    public long countByUserId(Integer userId) {
        return historyRepository.countByUser_Id(userId);
    }

    public long countByType(String type) {
        return historyRepository.countByType(type);
    }

    public long countByUserIdAndType(Integer userId, String type) {
        return historyRepository.countByUser_IdAndType(userId, type);
    }
}
