package com.example.demo.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.Entity.Leads;

@Repository
public interface LeadsRepo extends JpaRepository<Leads, Integer>{

}
