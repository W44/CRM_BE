package com.crmw.CRM_BE.repository;

import com.crmw.CRM_BE.entity.UserHistory;
import com.crmw.CRM_BE.entity.Users;
import com.crmw.CRM_BE.enums.HistoryTypes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUserHistoryRepository extends JpaRepository<UserHistory, Integer> {

    List<UserHistory> findByUser(Users user);
    List<UserHistory> findByUserAndType(Users user, HistoryTypes type);
    Optional<UserHistory> findByIdAndUser(Integer id, Users user);
    List<UserHistory> findByUser_Id(Integer userId);
    List<UserHistory> findByUser_Username(String username);
    long countByUser_Id(Integer userId);
    long countByType(String type);
    long countByUser_IdAndType(Integer userId, String type);

}
