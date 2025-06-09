package com.crmw.CRM_BE.repository;

import com.crmw.CRM_BE.entity.DonerContext;
import com.crmw.CRM_BE.entity.Doner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IDonerContextRepository extends JpaRepository<DonerContext, Integer> {
    List<DonerContext> findByDoner(Doner doner);
    List<DonerContext> findByDoner_Id(Integer donerId);

    
}
