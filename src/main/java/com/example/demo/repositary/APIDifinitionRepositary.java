package com.example.demo.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.APIDefinition;

@Repository("APIDifini")
public interface APIDifinitionRepositary extends JpaRepository<APIDefinition,Long>{
    List<APIDefinition> findByProjectId(Long projectId);
	
}


