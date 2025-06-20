package com.example.demo.service;

import com.example.demo.model.APIDefinition;
import com.example.demo.repositary.APIDifinitionRepositary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApiDefinitionService {

    private  APIDifinitionRepositary apiDefinitionRepository;

    @Autowired
    public ApiDefinitionService(APIDifinitionRepositary apiDefinitionRepository) {
        this.apiDefinitionRepository = apiDefinitionRepository;
    }

    // CREATE: Create a new APIDefinition
    public APIDefinition createApiDefinition(String name, String description) {
        APIDefinition apiDefinition = new APIDefinition();
        apiDefinition.setName(name);
        apiDefinition.setDescription(description);

        apiDefinitionRepository.save(apiDefinition);
        return apiDefinition;
    }

    // READ: Retrieve APIDefinition by ID
    public APIDefinition getApiDefinitionById(Long id) {
        return apiDefinitionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("API Definition not found"));
    }

    // UPDATE: Update an existing APIDefinition
    public APIDefinition updateApiDefinition(Long id, String name, String description) {
        APIDefinition apiDefinition = getApiDefinitionById(id);
        apiDefinition.setName(name);
        apiDefinition.setDescription(description);

        return apiDefinitionRepository.save(apiDefinition);
    }

    // DELETE: Delete APIDefinition by ID
    public void deleteApiDefinition(Long id) {
        APIDefinition apiDefinition = getApiDefinitionById(id);
        apiDefinitionRepository.delete(apiDefinition);
    }
}
