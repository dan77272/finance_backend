package com.myfinance.personal_finance_management.repository;

import com.myfinance.personal_finance_management.User;
import com.myfinance.personal_finance_management.model.Income;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRepository extends JpaRepository<Income, Long> {
    List<Income> findByUser(User user);

    @Query("SELECT COALESCE(SUM(i.amount), 0) FROM Income i WHERE i.user.id = :userId")
    double findTotalIncomeByUserId(Long userId);
}
