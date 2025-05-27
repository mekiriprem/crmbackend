package com.example.demo.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.DTO.LoginDto;
import com.example.demo.Entity.profiles;
import com.example.demo.Service.profilesService;

@RestController
@RequestMapping("/api")
public class profilesController {

    @Autowired
    
    private profilesService profilesService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto loginDTO) {
        Optional<profiles> optionalProfile = profilesService.login(loginDTO.getEmail(), loginDTO.getPassword());

        if (optionalProfile.isPresent()) {
            return ResponseEntity.ok(optionalProfile.get());
        } else {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody profiles profile) {
        String result = profilesService.register(profile);
        if ("Registration successful".equals(result)) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }
    
    @GetMapping("/bda-users")
    public ResponseEntity<?> getAllBDAUsers() {
        return ResponseEntity.ok(profilesService.getAllBDAs());
    }
    
    
}

