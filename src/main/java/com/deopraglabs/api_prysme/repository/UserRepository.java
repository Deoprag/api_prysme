package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Transactional
    @Query("""
        UPDATE User u SET u.email = CONCAT('deleted_', u.id, '@prysme.com.br'), \
        u.role = 'DELETED', \
        u.birthDate = local_date, \
        u.gender = 'U', \
        u.phoneNumber = :phoneNumber, \
        u.password = 'deleted', \
        u.active = false, \
        u.team = null WHERE u.id = :id""")
    int softDeleteById(long id, String phoneNumber);

    @Query("""
        SELECT COUNT(u) \
        FROM User u \
        WHERE u.id = :id \
        AND u.role = 'DELETED'""")
    int isDeleted(long id);

    List<User> findAllByActive(boolean active);
}
