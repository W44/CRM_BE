package com.crmw.CRM_BE.repository;

import com.crmw.CRM_BE.entity.Doner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IDonerRepository extends JpaRepository<Doner, Integer> {

    Doner findByName(String name);

    @Query("SELECT d FROM Doner d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :search, '%')) OR CAST(d.id AS string) LIKE CONCAT('%', :search, '%')")
    Page<Doner> findByNameContainingIgnoreCaseOrId(@Param("search") String search, Pageable pageable);

}
