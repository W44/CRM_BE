package com.crmw.CRM_BE.repository;

import com.crmw.CRM_BE.entity.Doner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDonerRepository extends JpaRepository<Doner, Integer> {

    Doner findByName(String name);

}
