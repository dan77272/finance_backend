package com.myfinance.personal_finance_management.service;

import org.springframework.stereotype.Service;

@Service
public class ReportService {

    public String generateEmailReport(String username, double totalIncome, double totalExpenses, double balance){
        return "<html>" +
                "<body>" +
                "<h2>Financial Report</h2>" +
                "<p>Hello, " + username + "!</p>" +
                "<p>Here’s your financial report summary:</p>" +
                "<p>Total Income: $" + totalIncome + "</p>" +
                "<p>Total Expenses: $" + totalExpenses + "</p>" +
                "<p>Balance: $" + balance + "</p>" +
                "<p>Thank you for using our app!</p>" +
                "</body>" +
                "</html>";
    }
}
