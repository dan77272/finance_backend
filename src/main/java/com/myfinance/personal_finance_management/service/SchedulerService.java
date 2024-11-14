package com.myfinance.personal_finance_management.service;

import com.myfinance.personal_finance_management.User;
import com.myfinance.personal_finance_management.UserRepository;
import com.myfinance.personal_finance_management.repository.ExpenseRepository;
import com.myfinance.personal_finance_management.repository.IncomeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchedulerService {

    @Autowired
    private EmailService emailService;

    @Autowired
    private  ReportService reportService;

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    @Scheduled(cron = "0 0 12 * * *") // Every day at noon
    public void sendDailyReport() {
        sendReportBasedOnFrequency("daily");
    }

    @Scheduled(cron = "0 0 12 * * SUN") // Every Sunday at noon
    public void sendWeeklyReport() {
        sendReportBasedOnFrequency("weekly");
    }

    @Scheduled(cron = "0 0 12 1 * *") // First day of every month at noon
    public void sendMonthlyReport() {
        sendReportBasedOnFrequency("monthly");
    }

    public void sendReportBasedOnFrequency(String frequency) {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            if (user.getIsSubscribed() && frequency.equals(user.getReportFrequency())) { // Check subscription
                Long userId = user.getId();
                double totalIncome = incomeRepository.findTotalIncomeByUserId(userId);
                double totalExpenses = expenseRepository.findTotalExpensesByUserId(userId);
                double balance = totalIncome - totalExpenses;

                String emailContent = reportService.generateEmailReport(user.getUsername(), totalIncome, totalExpenses, balance);
                try {
                    emailService.sendEmail("danysanioura@gmail.com", "Your " + frequency.substring(0, 1).toUpperCase() + frequency.substring(1) + " Financial Report", emailContent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
