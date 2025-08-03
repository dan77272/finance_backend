package com.myfinance.personal_finance_management;

import com.myfinance.personal_finance_management.dto.LoginRequest;
import com.myfinance.personal_finance_management.exception.UserNotFoundException;
import com.myfinance.personal_finance_management.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    public ResponseEntity<?> registerUser(User user){
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            return ResponseEntity.badRequest().body("Username already taken");
        }
        try{
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User registeredUser = userRepository.save(user);
            return ResponseEntity.ok(registeredUser);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error registering user");
        }
    }

    public Map<String, Object> loginUser(LoginRequest loginRequest){
        Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());

        Map<String, Object> result = new HashMap<>();
        if(user.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())){
            String token = jwtUtil.generateToken(user.get().getUsername());
            result.put("token", token);
            result.put("body", Map.of("userId", user.get().getId().toString(), "message", "Login successful"));
        }else{
            result.put("token", null);
            result.put("body", Map.of("message", "Email or password is incorrect"));
        }
        return result;
    }

    public Map<String, String> getUserProfile(String token) {
        System.out.println("Received JWT in service: " + token); // debug

        String username = jwtUtil.extractUsername(token); // Make sure this doesn't throw!
        System.out.println("Decoded username: " + username);

        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            Map<String, String> response = new HashMap<>();
            response.put("username", user.get().getUsername());
            response.put("userId", user.get().getId().toString());
            return response;
        } else {
            throw new UserNotFoundException("User not found");
        }
    }


    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


}
