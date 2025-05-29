package com.example.demo.DTO;

import com.example.demo.Entity.LeadUpdateHistory;
import lombok.Data;

import java.util.Date;

@Data
public class LeadUpdateHistoryDto {
    private String status;
    private String actionStatus;
    private String remarks;
    private String actionTaken;
    private Date updatedAt;
    private String updatedBy;

    public LeadUpdateHistoryDto(LeadUpdateHistory history) {
        this.status = history.getStatus();
        this.actionStatus = history.getActionStatus();
        this.remarks = history.getRemarks();
        this.actionTaken = history.getActionTaken();
        this.updatedAt = history.getUpdatedAt();
        this.updatedBy = history.getUpdatedByUserId().getName(); // or getName() depending on your Profiles entity
    }
}
