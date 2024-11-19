package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PermissionRepository extends JpaRepository<Permission, Long> {

    @Modifying
    @Query("DELETE FROM Permission p WHERE p.id = :id")
    int deleteById(@Param("id") long id);

    Permission findByDescription(String description);
}
