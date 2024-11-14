package com.myfinance.personal_finance_management.controller;

import com.myfinance.personal_finance_management.User;
import com.myfinance.personal_finance_management.UserRepository;
import com.myfinance.personal_finance_management.model.Expense;
import com.myfinance.personal_finance_management.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expense")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public Expense addExpense(@RequestBody Expense expense, @RequestParam Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        expense.setUser(user);
        return expenseService.addExpense(expense);
    }

    @GetMapping("/user/{userId}")
    public List<Expense> getExpensesByUser(@PathVariable Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return expenseService.getExpensesByUser(user);
    }
}