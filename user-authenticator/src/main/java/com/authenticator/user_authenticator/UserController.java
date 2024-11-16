package com.authenticator.user_authenticator;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User user) {
        
        Map<String, Object> response = new HashMap<>();
        
        if (userService.addNewUser(user.getUsername(), user.getPassword(), "watchlist-app")) {
            response.put("message", "User registered successfully");
            response.put("success", true);
        } else {
            response.put("message", "Username already taken");
            response.put("success", false);
        }
        
        return ResponseEntity.ok(response);
    }



    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        
        boolean authenticated = userService.authenticateUser(user.getUsername(), user.getPassword(), "watchlist-app");
        
        if (authenticated) {
            response.put("message", "Login successful");
            response.put("success", true);
        } else {
            response.put("message", "Invalid credentials");
            response.put("success", false);
        }
        
        return ResponseEntity.ok(response);
    }

}
