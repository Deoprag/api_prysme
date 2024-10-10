package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {

    @Modifying
    @Query("DELETE FROM Team t WHERE t.id = :id")
    int deleteById(@Param("id") long id);
}
