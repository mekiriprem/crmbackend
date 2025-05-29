package com.example.demo.Controller;


import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.DTO.LeadUpdateDto;
import com.example.demo.DTO.LeadUpdateHistoryDto;
import com.example.demo.Entity.LeadUpdateHistory;
import com.example.demo.Entity.Leads;
import com.example.demo.Repository.LeadsRepo;
import com.example.demo.Service.LeadsService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;

@RestController
@RequestMapping("/api/leads")
public class LeadsController {

    @Autowired
    private LeadsService leadsService;

    @Autowired
    private LeadsRepo leadsRepo;
 
    // ✅ Upload CSV
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        String message = leadsService.uploadCSV(file);
        return ResponseEntity.ok().body(message);
    }

    // ✅ Get all leads
    @GetMapping("/getall")
    public List<Leads> getAllLeads() {
        return leadsRepo.findAll();
    }

    // ✅ Update a lead
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateLead(
            @PathVariable Integer id,
            @RequestBody LeadUpdateDto dto
    ) {
        String result = leadsService.updateLead(id, dto);
        if (result.contains("successfully")) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(404).body(result);
        }
    }

    // ✅ Export leads to CSV
    @GetMapping("/export")
    public void exportLeads(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=leads_export.csv");
        leadsService.writeLeadsToCsv(response.getOutputStream());
    }

    // ✅ Lead assignment request structure
    @Data
    public static class LeadAssignmentRequest {
        private List<String> leadIds;
        private String bdaId;
        private String bdaName;
    }


    
    @PostMapping("/assign")
    public String assignLeads(@RequestBody LeadAssignmentRequest request) {
        return leadsService.assignLeads(request.getLeadIds(), request.getBdaId(), request.getBdaName());
    }
    
    @GetMapping("/{leadId}/history")
    public List<LeadUpdateHistoryDto> getLeadHistory(@PathVariable Integer leadId) {
        return leadsService.getHistoryByLeadId(leadId).stream()
                .map(LeadUpdateHistoryDto::new)
                .toList();
    }
    @GetMapping("/history")
    public List<LeadUpdateHistoryDto> getAllLeadHistory() {
        return leadsService.getAllHistory().stream()
                .map(LeadUpdateHistoryDto::new)
                .toList();
    }

    // ✅ Get history by user ID
    @GetMapping("/history/user/{userId}")
    public List<LeadUpdateHistoryDto> getHistoryByUserId(@PathVariable Integer userId) {
        return leadsService.getHistoryByUserId(userId).stream()
                .map(LeadUpdateHistoryDto::new)
                .toList();
    }
    
    



  
}
