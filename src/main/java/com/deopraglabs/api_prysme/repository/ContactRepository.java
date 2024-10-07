package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Modifying
    @Query("DELETE FROM Contact c WHERE c.id = :id")
    int deleteById(@Param("id") long id);
}