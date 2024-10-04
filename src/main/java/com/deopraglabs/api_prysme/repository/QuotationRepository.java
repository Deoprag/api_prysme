package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long> {

    @Modifying
    @Query("DELETE FROM Quotation q WHERE q.id = :id")
    int deleteById(@Param("id") long id);
}
