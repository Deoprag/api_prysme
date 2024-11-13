package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Modifying
    @Query("DELETE FROM Task t WHERE t.id = :id")
    int deleteById(@Param("id") long id);

    List<Task> findAllByUserIdAndDueDate(Long userId, Date dueDate);
}
