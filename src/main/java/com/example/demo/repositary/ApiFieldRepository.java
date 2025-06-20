package com.example.demo.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.model.ApiField;

public interface ApiFieldRepository extends JpaRepository<ApiField, Long> {
    List<ApiField> findByApiDefinitionId(Long apiDefinitionId);

}