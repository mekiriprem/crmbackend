package com.example.demo.DTO;

import java.util.List;


import lombok.Data;

@Data
public class BulkDeleteDto {
   
    private List<Integer> leadIds;
}