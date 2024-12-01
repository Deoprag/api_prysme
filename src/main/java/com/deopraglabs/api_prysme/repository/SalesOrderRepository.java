package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {

    @Modifying
    @Query("DELETE FROM SalesOrder so WHERE so.id = :id")
    int deleteById(@Param("id") long id);

    List<SalesOrder> findAllByCustomerId(long id);

    List<SalesOrder> findAllBySellerId(long id);
}
