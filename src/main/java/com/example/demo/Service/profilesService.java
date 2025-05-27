package com.example.demo.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.example.demo.Entity.profiles;
import com.example.demo.Repository.profilesRepo;
import org.apache.commons.csv.CSVParser;

@Service
public class profilesService {
    @Autowired
    private profilesRepo profilesRepository;

    public Optional<profiles> login(String email, String password) {
        return profilesRepository.findByEmailAndPassword(email, password);
    }
    
    public String register(profiles profile) {
        Optional<profiles> existingProfile = profilesRepository.findByEmail(profile.getEmail()); // ✅

        if (existingProfile.isPresent()) {
            return "Email already registered";
        }
        profile.setRole("BDA");
        profilesRepository.save(profile);
        return "Registration successful";
        
    }
    
    public List<profiles> getAllBDAs() {
        return profilesRepository.findByRole("BDA");
    }
    
    
    

}
