package com.example.demo.Repository;

import com.example.demo.Entity.profiles; // ✅ Correct import

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface profilesRepo extends JpaRepository<profiles, Integer> {

    @Query("SELECT p FROM profiles p WHERE p.email = :email AND p.password = :password")
    Optional<profiles> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);


    Optional<profiles> findByEmail(String email);
    
    List<profiles> findByRole(String role);
}

