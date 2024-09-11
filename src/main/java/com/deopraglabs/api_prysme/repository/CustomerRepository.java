package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.Customer;
import com.deopraglabs.api_prysme.data.model.CustomerStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    @Modifying
    @Transactional
    @Query("""
        UPDATE Customer c SET c.cpfCnpj = :cpf, \
        c.name = "", \
        c.tradeName = "DELETED", \
        c.email = CONCAT('deleted_client_', c.id, '@prysme.com.br'), \
        c.birthFoundationDate = local_date, \
        c.stateRegistration = null, \
        c.customerStatus = 'DELETED', \
        c.phoneNumbers = null, \
        c.address = null, \
        c.cart = null WHERE c.id = :id""")
    int softDeleteById(long id, String cpf);

    @Query("""
        SELECT COUNT(c) \
        FROM Customer c \
        WHERE c.id = :id \
        AND c.customerStatus = 'DELETED'""")
    int isDeleted(long id);

    List<Customer> findAllByCustomerStatusNot(CustomerStatus customerStatus);
}
