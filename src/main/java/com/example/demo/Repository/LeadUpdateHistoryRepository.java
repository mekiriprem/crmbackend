package com.example.demo.Repository;

import com.example.demo.Entity.LeadUpdateHistory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadUpdateHistoryRepository extends JpaRepository<LeadUpdateHistory, Integer> {
    
    List<LeadUpdateHistory> findByLeadIdOrderByUpdatedAtDesc(Integer leadId);
    List<LeadUpdateHistory> findAllByOrderByUpdatedAtDesc();
    List<LeadUpdateHistory> findByUpdatedByUserIdIdOrderByUpdatedAtDesc(Integer userId);


}

