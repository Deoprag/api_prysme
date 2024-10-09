package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.NF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NFRepository extends JpaRepository<NF, Long> {

    @Modifying
    @Query("DELETE FROM NF nf WHERE nf.id = :id")
    int deleteById(@Param("id") long id);
}
