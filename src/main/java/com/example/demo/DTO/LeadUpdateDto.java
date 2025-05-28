package com.example.demo.DTO;


import lombok.Data;
import java.util.Date;
import java.util.Optional;


import java.util.List;

@Data
public class LeadUpdateDto {
    private String name;
    private String contactNo;
    private String email;
    private String industry;
    private String service;
    private String type;
    private String status;
    private String assignedTo;
    private Integer loggedinId;
    private Date followUp;
    private String temperature;
    private String intrests; // ✅ fix this
    private String remarks;
    private String companyName;
    private String city;
    private String state;
    private String actionStatus;
    private String actionTaken;
    private String createdAt;
    private String lastUpdated;
    


    // Getters and setters...
}

