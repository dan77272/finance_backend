package com.myfinance.personal_finance_management.repository;

import com.myfinance.personal_finance_management.User;
import com.myfinance.personal_finance_management.model.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByUser(User user);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user.id = :userId")
    double findTotalExpensesByUserId(Long userId);
}
