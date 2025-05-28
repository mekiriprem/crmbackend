package com.example.demo.Entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@lombok.Data
public class LeadUpdateHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "lead_id", nullable = false)
    private Leads lead;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private String actionStatus;

    @Column(nullable = false)
    private String remarks;

    @Column(nullable = false)
    private String actionTaken;

    @Column(nullable = false)
    private Date updatedAt;
    @ManyToOne
    @JoinColumn(name = "update_by", nullable = false)
    private profiles updatedByUserId;

 // From Profile/User table
}
