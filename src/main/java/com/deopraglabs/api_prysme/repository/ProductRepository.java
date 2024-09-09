package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Modifying
    @Query("DELETE FROM Product p WHERE p.id = :id")
    int deleteById(@Param("id") long id);

}
