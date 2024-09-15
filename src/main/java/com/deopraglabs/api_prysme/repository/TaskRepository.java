package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {}
