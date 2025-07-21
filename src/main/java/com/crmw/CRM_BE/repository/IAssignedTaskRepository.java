package com.crmw.CRM_BE.repository;

import com.crmw.CRM_BE.entity.AssignedTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IAssignedTaskRepository extends JpaRepository<AssignedTask, Long> {

    List<AssignedTask> findByStatusAndAssignedToId(String status, Long userId);

    List<AssignedTask> findByAssignedToId(Long userId);

    @Query("""
            SELECT t FROM AssignedTask t
            WHERE
                (LOWER(t.title) LIKE LOWER(CONCAT('%', :searchTerm, '%')) 
                OR LOWER(t.description) LIKE LOWER(CONCAT('%', :searchTerm, '%'))
                OR (:searchId IS NOT NULL AND t.assignedToId = :searchId))
            """)
    List<AssignedTask> searchTasksPaged(@Param("searchTerm") String searchTerm,
                                        @Param("searchId") Integer searchId);
}

