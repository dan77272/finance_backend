package com.myfinance.personal_finance_management.service;

import com.myfinance.personal_finance_management.User;
import com.myfinance.personal_finance_management.model.Income;
import com.myfinance.personal_finance_management.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IncomeService {

    @Autowired
    private IncomeRepository incomeRepository;

    public Income addIncome(Income income){
        return incomeRepository.save(income);
    }

    public List<Income> getIncomeByUser(User user){
        return incomeRepository.findByUser(user);
    }
}
