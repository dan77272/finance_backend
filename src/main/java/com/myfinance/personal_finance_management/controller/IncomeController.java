package com.myfinance.personal_finance_management.controller;

import com.myfinance.personal_finance_management.User;
import com.myfinance.personal_finance_management.UserRepository;
import com.myfinance.personal_finance_management.model.Income;
import com.myfinance.personal_finance_management.service.IncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/income")
public class    IncomeController {

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/add")
    public Income addIncome(@RequestBody Income income, @RequestParam Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        income.setUser(user);
        return incomeService.addIncome(income);
    }

    @GetMapping("/user/{userId}")
    public List<Income> getIncomeByUser(@PathVariable Long userId){
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return incomeService.getIncomeByUser(user);
    }
}
