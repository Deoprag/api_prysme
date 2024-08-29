package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {}
