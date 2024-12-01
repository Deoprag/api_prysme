package com.deopraglabs.api_prysme.repository;

import com.deopraglabs.api_prysme.data.model.Goal;
import com.deopraglabs.api_prysme.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface GoalRepository extends JpaRepository<Goal, Long> {

    @Modifying
    @Query("DELETE FROM Goal g WHERE g.id = :id")
    int deleteById(@Param("id") long id);

    Goal findTopBySellerIdOrderByCreatedDateDesc(long sellerId);

    @Query("SELECT COALESCE(SUM(nf.totalValue), 0) " +
            "FROM Goal g " +
            "LEFT JOIN NF nf ON nf.seller = g.seller AND nf.seller = :seller " +
            "WHERE nf.createdDate BETWEEN g.startDate AND g.endDate " +
            "AND g.id = :goalId")
    BigDecimal getCurrentProgress(@Param("seller") User seller, @Param("goalId") Long goalId);
}
