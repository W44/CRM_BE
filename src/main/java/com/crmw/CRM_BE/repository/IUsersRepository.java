package com.crmw.CRM_BE.repository;
import com.crmw.CRM_BE.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface IUsersRepository extends JpaRepository<Users, Integer> {
    Optional<Users> findByUsername(String username);

}
