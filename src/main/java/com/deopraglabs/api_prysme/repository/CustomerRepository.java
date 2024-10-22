package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.Customer;
import com.deopraglabs.api_prysme.data.model.CustomerStatus;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Customer findByCpfCnpjAndIdNot(String cpfCnpj, long id);

    Customer findByEmailAndIdNot(String email, long id);

    Customer findByStateRegistrationAndIdNot(String stateRegistration, long id);

    List<Customer> findAllByCustomerStatusNot(CustomerStatus customerStatus);

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
    int softDeleteById(@Param("id") long id, @Param("cpf") String cpf);

    @Query("""
            SELECT COUNT(c) \
            FROM Customer c \
            WHERE c.id = :id \
            AND c.customerStatus = 'DELETED'""")
    int isDeleted(long id);

}
