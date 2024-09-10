package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    @Modifying
    @Query("DELETE FROM ProductCategory p WHERE p.id = :id")
    int deleteById(@Param("id") long id);
}
