package com.crmw.CRM_BE.controllers;


import com.crmw.CRM_BE.entity.UserHistory;
import com.crmw.CRM_BE.enums.HistoryTypes;
import com.crmw.CRM_BE.service.UserHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/userhistory")
public class UserHistoryController {
    @Autowired
    private UserHistoryService historyService;

    @GetMapping("/user/{userId}")
    public List<UserHistory> getByUserId(@PathVariable Integer userId) {
        return historyService.getByUserId(userId);
    }

    @GetMapping("/username/{username}")
    public List<UserHistory> getByUsername(@PathVariable String username) {
        return historyService.getByUsername(username);
    }

    @GetMapping("/count/user/{userId}")
    public long countByUser(@PathVariable Integer userId) {
        return historyService.countByUserId(userId);
    }

    @GetMapping("/count/type/{typeId}")
    public long countByType(@PathVariable Integer typeId) {
        String type;
        try {
            type = HistoryTypes.values()[typeId].toString();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Invalid History Type ID: " + typeId);
        }
        return historyService.countByType(type);
    }

    @GetMapping("/count/user/{userId}/type/{typeId}")
    public long countByUserAndType(@PathVariable Integer userId, @PathVariable Integer typeId) {
        String type;
        try {
            type = HistoryTypes.values()[typeId].toString();
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Invalid History Type ID: " + typeId);
        }
        return historyService.countByUserIdAndType(userId, type);
    }
}
