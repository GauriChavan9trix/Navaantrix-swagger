package com.example.demo.repositary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.ApiHeader;

@Repository
public interface ApiHeaderRepository extends JpaRepository<ApiHeader, Long> {
	List<ApiHeader> findByApiDefinitionId(Long apiDefinitionId);
}