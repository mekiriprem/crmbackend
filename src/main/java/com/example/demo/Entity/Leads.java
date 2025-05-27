package com.example.demo.Entity;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@lombok.Data
public class Leads {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String Name;
    private String ContactNo;
    private String Email;
    private String Status;
    private String ActionStatus;
    private String AssignedTo;
    private String interests;  
    private String Remarks;
    private String ActionTaken;
  
    private Date  FollowUp;
    private Date  LastUpdated;
    
    private String CompanyName;
    private String Industry;
    private String City;
    private String State;
    

}
