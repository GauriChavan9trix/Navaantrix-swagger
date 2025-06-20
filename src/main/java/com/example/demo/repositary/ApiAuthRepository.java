package com.example.demo.repositary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.ApiAuth;

@Repository
public interface ApiAuthRepository extends JpaRepository<ApiAuth, Long> {
    Optional<ApiAuth> findByApiDefinitionId(Long apiDefinitionId);
}
