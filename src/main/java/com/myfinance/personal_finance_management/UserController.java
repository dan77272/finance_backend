package com.myfinance.personal_finance_management;

import com.myfinance.personal_finance_management.dto.LoginRequest;
import com.myfinance.personal_finance_management.service.EmailService;
import com.myfinance.personal_finance_management.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ReportService reportService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user){
        return userService.registerUser(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest){
        return userService.loginUser(loginRequest);
    }

    @GetMapping("/users")
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @PostMapping("/subscribe")
    public ResponseEntity<?> subscribeToReports(@RequestBody SubscriptionRequest request){
        Long userId = request.getUserId();
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setIsSubscribed(true);
            user.setReportFrequency(request.getFrequency()); // Save frequency choice

            userRepository.save(user); // Save subscription status and frequency

            System.out.println("User subscribed with frequency: " + request.getFrequency());
            return ResponseEntity.ok("Subscribed to " + request.getFrequency() + " reports");
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    @GetMapping("/test-email")
    public ResponseEntity<?> sendTestEmail(){
        String emailContent = reportService.generateEmailReport("Test User", 1000, 500, 500);
        try {
            // Replace with your email address for testing
            emailService.sendEmail("danysanioura@gmail.com", "Test Financial Report", emailContent);
            return ResponseEntity.ok("Test email sent successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to send test email.");
        }
    }


}

class SubscriptionRequest {
    private String frequency;
    private Long userId;

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

