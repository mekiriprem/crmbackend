package com.example.demo.Service;

import com.example.demo.DTO.LeadUpdateDto;
import com.example.demo.Entity.LeadUpdateHistory;
import com.example.demo.Entity.Leads;
import com.example.demo.Entity.profiles;
import com.example.demo.Repository.LeadUpdateHistoryRepository;
import com.example.demo.Repository.LeadsRepo;
import com.example.demo.Repository.profilesRepo;

import org.apache.commons.csv.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.Profiles;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LeadsService {

    @Autowired
    private LeadsRepo leadsRepository;
    
    @Autowired
    private LeadUpdateHistoryRepository historyRepository;
    
    @Autowired
    private profilesRepo  profilesRepository;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public String uploadCSV(MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream())) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            List<Leads> leadsList = new ArrayList<>();
            List<String> skippedEmails = new ArrayList<>();

            for (CSVRecord record : csvParser) {
                String email = getValue(record, "Email");

                // ❌ Skip if email already exists
                if (email != null && leadsRepository.existsByEmail(email)) {
                    skippedEmails.add(email);
                    continue;
                }

                Leads lead = new Leads();

                lead.setName(getValue(record, "Name"));
                lead.setContactNo(getValue(record, "ContactNo"));
                lead.setEmail(email);
                lead.setStatus(getValue(record, "Status"));
                lead.setActionStatus(getValue(record, "ActionStatus"));
                lead.setAssignedTo(getValue(record, "AssignedTo"));
                lead.setIntrests(getValue(record, "Intrests"));
                lead.setRemarks(getValue(record, "Remarks"));
                lead.setActionTaken(getValue(record, "ActionTaken"));
                lead.setCompanyName(getValue(record, "CompanyName"));
                lead.setIndustry(getValue(record, "Industry"));
                lead.setCity(getValue(record, "City"));
                lead.setState(getValue(record, "State"));

                try {
                    String followUpStr = getValue(record, "FollowUp");
                    lead.setFollowUp((followUpStr != null && !followUpStr.isBlank())
                            ? sdf.parse(followUpStr)
                            : null);
                } catch (Exception e) {
                    lead.setFollowUp(null);
                }

                lead.setLastUpdated(new Date());
                leadsList.add(lead);
            }

            leadsRepository.saveAll(leadsList);

            StringBuilder response = new StringBuilder("Upload successful! Imported ")
                    .append(leadsList.size())
                    .append(" leads.");

            if (!skippedEmails.isEmpty()) {
                response.append(" Skipped ").append(skippedEmails.size())
                        .append(" existing emails: ")
                        .append(String.join(", ", skippedEmails));
            }

            return response.toString();

        } catch (Exception e) {
            return "Upload failed: " + e.getMessage();
        }
    }



    public void writeLeadsToCsv(OutputStream os) throws IOException {
        List<Leads> leads = leadsRepository.findAll();

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
             CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader(
                     "Id", "Name", "ContactNo", "Email", "Status", "ActionStatus",
                     "AssignedTo", "Intrests", "Remarks", "ActionTaken", "CompanyName",
                     "Industry", "City", "State", "FollowUp", "LastUpdated"
             ))) {

            for (Leads lead : leads) {
                csvPrinter.printRecord(
                        lead.getId(),
                        lead.getName(),
                        lead.getContactNo(),
                        lead.getEmail(),
                        lead.getStatus(),
                        lead.getActionStatus(),
                        lead.getAssignedTo(),
                        lead.getIntrests(),
                        lead.getRemarks(),
                        lead.getActionTaken(),
                        lead.getCompanyName(),
                        lead.getIndustry(),
                        lead.getCity(),
                        lead.getState(),
                        lead.getFollowUp() != null ? sdf.format(lead.getFollowUp()) : "",
                        lead.getLastUpdated() != null ? sdf.format(lead.getLastUpdated()) : ""
                );
            }
        }
    }

    public String assignLeads(List<String> leadIds, String bdaId, String bdaName) {
        List<Integer> intIds = leadIds.stream().map(Integer::parseInt).toList();
        List<Leads> leads = leadsRepository.findAllById(intIds);

        for (Leads lead : leads) {
            lead.setAssignedTo(bdaName); // You can also store bdaId if you have a column
            lead.setLastUpdated(new Date());
        }

        leadsRepository.saveAll(leads);
        return "Assigned successfully";
    }

    public String updateLead(Integer id, LeadUpdateDto dto) {
        Optional<Leads> optionalLead = leadsRepository.findById(id);
        if (optionalLead.isEmpty()) {
            return "Lead not found with ID: " + id;
        }

        Leads lead = optionalLead.get();

        if (dto.getName() != null) lead.setName(dto.getName());
        if (dto.getEmail() != null) lead.setEmail(dto.getEmail());
        if (dto.getContactNo() != null) lead.setContactNo(dto.getContactNo());
        if (dto.getStatus() != null) lead.setStatus(dto.getStatus());
        if (dto.getActionStatus() != null) lead.setActionStatus(dto.getActionStatus());
        if (dto.getAssignedTo() != null) lead.setAssignedTo(dto.getAssignedTo());
        if (dto.getIntrests() != null) lead.setIntrests(dto.getIntrests());
        if (dto.getRemarks() != null) lead.setRemarks(dto.getRemarks());
        if (dto.getActionTaken() != null) lead.setActionTaken(dto.getActionTaken());
        if (dto.getFollowUp() != null) lead.setFollowUp(dto.getFollowUp());


        lead.setLastUpdated(new Date());
        
        LeadUpdateHistory history = new LeadUpdateHistory();
        
        history.setLead(lead);
        
        profiles user = profilesRepository.findById(dto.getLoggedinId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + dto.getLoggedinId()));
            history.setUpdatedByUserId(user);


       
       
        history.setRemarks(dto.getRemarks());
        history.setStatus(dto.getStatus());
        history.setActionStatus(dto.getActionStatus());
        history.setActionTaken(dto.getActionTaken());
        history.setUpdatedAt(new Date());

        historyRepository.save(history);
        

        leadsRepository.save(lead);
        return "Lead updated successfully";
    }

    // Utility method to safely get CSV value
    private String getValue(CSVRecord record, String column) {
        return record.isMapped(column) && !record.get(column).isEmpty() ? record.get(column) : "";
    }
    
    public List<LeadUpdateHistory> getHistoryByLeadId(Integer leadId) {
        return historyRepository.findByLeadIdOrderByUpdatedAtDesc(leadId);
    }
    
    public List<LeadUpdateHistory> getAllHistory() {
        return historyRepository.findAllByOrderByUpdatedAtDesc();
    }
    public List<LeadUpdateHistory> getHistoryByUserId(Integer userId) {
        return historyRepository.findByUpdatedByUserIdIdOrderByUpdatedAtDesc(userId);
    }

    public interface LeadService {
        void bulkDeleteLeads(List<Long> leadIds) throws IllegalArgumentException;
    }

    
    public String deleteSelectedLeads(List<String> leadIds) {
        List<Integer> intIds = leadIds.stream().map(Integer::parseInt).toList();
        List<Leads> leads = leadsRepository.findAllById(intIds);

        if (leads.isEmpty()) {
            return "No leads found with the provided IDs.";
        }

        // Optional: delete associated lead update history if needed
        for (Leads lead : leads) {
            historyRepository.deleteAll(
                historyRepository.findByLeadIdOrderByUpdatedAtDesc(lead.getId())
            );
        }

        leadsRepository.deleteAll(leads);
        return "Deleted " + leads.size() + " lead(s) successfully.";
    }



    
  

}
