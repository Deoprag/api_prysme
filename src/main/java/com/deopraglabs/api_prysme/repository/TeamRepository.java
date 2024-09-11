package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {}
