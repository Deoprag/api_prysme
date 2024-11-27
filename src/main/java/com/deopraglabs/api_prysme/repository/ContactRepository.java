package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.Contact;
import com.deopraglabs.api_prysme.data.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Modifying
    @Query("DELETE FROM Contact c WHERE c.id = :id")
    int deleteById(@Param("id") long id);

    List<Contact> findAllByCustomer(Customer customer);
}
