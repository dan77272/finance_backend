package com.myfinance.personal_finance_management;

import com.myfinance.personal_finance_management.dto.LoginRequest;
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

    public ResponseEntity<?> loginUser(LoginRequest loginRequest){
        Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());

        if(user.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())){
            String token = jwtUtil.generateToken(user.get().getUsername());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("userId", user.get().getId().toString());
            return ResponseEntity.ok(response);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }


}
