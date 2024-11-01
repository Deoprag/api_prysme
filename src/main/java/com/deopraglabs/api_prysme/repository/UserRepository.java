package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Modifying
    @Transactional
    @Query("""
            UPDATE User u SET u.email = CONCAT('deleted_user_', u.id, '@prysme.com.br'), \
            u.birthDate = local_date, \
            u.gender = 'U', \
            u.phoneNumber = :phoneNumber, \
            u.password = 'deleted', \
            u.accountNonExpired = false, \
            u.accountNonLocked = false, \
            u.credentialsNonExpired = false, \
            u.enabled = false, \
            u.team = null WHERE u.id = :id""")
    int softDeleteById(long id, String phoneNumber);

    @Query("""
            SELECT COUNT(u) \
            FROM User u \
            WHERE u.id = :id \
            AND u.enabled = true""")
    int isDeleted(long id);

    List<User> findAllByEnabled(boolean enabled);

    User findByEmailAndIdNot(String email, long id);

    User findByUsername(String username);

    User findByPhoneNumberAndIdNot(String phoneNumber, long id);
}
