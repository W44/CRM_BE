package com.crmw.CRM_BE.repository;

import com.crmw.CRM_BE.entity.AssignedTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;


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

    @Query(value = """
            SELECT * FROM assigned_task t
             WHERE ( CAST(:query AS TEXT) IS NULL
                 OR LOWER(t.title) LIKE LOWER(CONCAT('%', CAST(:query AS TEXT), '%'))
                 OR LOWER(t.description) LIKE LOWER(CONCAT('%', CAST(:query AS TEXT), '%')) )
               AND ( :status IS NULL OR t.status = CAST(:status AS TEXT) )
               AND t.assigned_to_name = :username
            """,
            nativeQuery = true)
    Page<AssignedTask> searchMyTasks(@Param("query") String query,
                                     @Param("status") String status,
                                     @Param("username") String username,
                                     Pageable pageable);

    @Query(value = """
            SELECT * FROM assigned_task t
             WHERE ( CAST(:query AS TEXT) IS NULL
                 OR LOWER(t.title) LIKE LOWER(CONCAT('%', CAST(:query AS TEXT), '%'))
                 OR LOWER(t.description) LIKE LOWER(CONCAT('%', CAST(:query AS TEXT), '%')) )
               AND ( :status IS NULL OR t.status = CAST(:status AS TEXT) )
            """,
            nativeQuery = true)
    Page<AssignedTask> searchAllTasks(@Param("query") String query,
                                      @Param("status") String status,
                                      Pageable pageable);
}


