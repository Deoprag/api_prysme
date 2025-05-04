package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.enums.CustomerStatus;
import com.deopraglabs.api_prysme.data.model.Customer;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    Customer findByCpfCnpjAndIdNot(String cpfCnpj, UUID id);

    Customer findByEmailAndIdNot(String email, UUID id);

    Customer findByStateRegistrationAndIdNot(String stateRegistration, UUID id);

    List<Customer> findAllByCustomerStatusNot(CustomerStatus customerStatus);

    List<Customer> findAllBySellerId(UUID id);

    @Modifying
    @Transactional
    @Query("""
            UPDATE Customer c SET c.cpfCnpj = :cpf, \
            c.name = CONCAT('DELETED CUSTOMER (', c.id, ')'), \
            c.tradeName = CONCAT('DELETED CUSTOMER (', c.id, ')'), \
            c.email = CONCAT('deleted_customer_', c.id, '@prysme.com.br'), \
            c.birthFoundationDate = CURRENT_DATE , \
            c.stateRegistration = null, \
            c.lastModifiedDate = CURRENT_TIMESTAMP, \
            c.customerStatus = 'DELETED' WHERE c.id = :id""")
    int softDeleteById(@Param("id") UUID id, @Param("cpf") String cpf);

    @Query("""
            SELECT COUNT(c) \
            FROM Customer c \
            WHERE c.id = :id \
            AND c.customerStatus = 'DELETED'""")
    int isDeleted(UUID id);

    long countCustomerByCustomerStatusNot(CustomerStatus customerStatus);

    long countCustomerByCustomerStatus(CustomerStatus customerStatus);

    @Modifying
    @Query("UPDATE Customer c SET c.customerStatus = :status WHERE c.id = :id")
    void updateCustomerStatus(@Param("id") UUID id, @Param("status") CustomerStatus status);
}
