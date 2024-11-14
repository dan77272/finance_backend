package com.myfinance.personal_finance_management.service;

import com.myfinance.personal_finance_management.User;
import com.myfinance.personal_finance_management.model.Expense;
import com.myfinance.personal_finance_management.repository.ExpenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    public Expense addExpense(Expense expense){
        return expenseRepository.save(expense);
    }

    public List<Expense> getExpensesByUser(User user){
        return expenseRepository.findByUser(user);
    }
}
