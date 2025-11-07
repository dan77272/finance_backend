package com.myfinance.personal_finance_management;

import com.myfinance.personal_finance_management.dto.LoginRequest;
import com.myfinance.personal_finance_management.exception.UserNotFoundException;
import com.myfinance.personal_finance_management.service.EmailService;
import com.myfinance.personal_finance_management.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "https://financefrontend-production.up.railway.app/")
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
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        Map<String, Object> loginResult = userService.loginUser(loginRequest);
        String token = (String) loginResult.get("token");
        Object body = loginResult.get("body");

        if(token != null){
            ResponseCookie cookie = ResponseCookie.from("token", token)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .sameSite("Lax")
                    .maxAge(24 * 60* 60)
                    .build();
            response.addHeader("Set-Cookie", cookie.toString());
            return ResponseEntity.ok(body);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(body);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("token", "")
                .httpOnly(true)
                .secure(false)
                .path("/")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
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

    @GetMapping("/profile")
    public ResponseEntity<?> getUserProfile(@CookieValue(value = "token", required = false) String token) {
        System.out.println("Cookie token: " + token); // debug
        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Not logged in"));
        }
        try {
            Map<String, String> userProfile = userService.getUserProfile(token);
            return ResponseEntity.ok(userProfile);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            // If the token is invalid (expired/invalid signature), return unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("message", "Invalid token"));
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

