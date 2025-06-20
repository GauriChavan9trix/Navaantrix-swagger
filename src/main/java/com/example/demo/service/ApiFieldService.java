package com.example.demo.service;

import com.example.demo.model.ApiField;
import com.example.demo.repositary.APIDifinitionRepositary;
import com.example.demo.repositary.ApiFieldRepository;
import com.example.demo.model.APIDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiFieldService {

    private final ApiFieldRepository apiFieldRepository;
    private final APIDifinitionRepositary apiDefinitionRepository;

    @Autowired
    public ApiFieldService(ApiFieldRepository apiFieldRepository, APIDifinitionRepositary apiDefinitionRepository) {
        this.apiFieldRepository = apiFieldRepository;
        this.apiDefinitionRepository = apiDefinitionRepository;
    }

    // CREATE: Create a new ApiField
    public ApiField createApiField(Long apiDefinitionId, String name, String type, String example, boolean required) {
        APIDefinition apiDefinition = apiDefinitionRepository.findById(apiDefinitionId)
                .orElseThrow(() -> new RuntimeException("API Definition not found"));

        ApiField apiField = new ApiField();
        apiField.setName(name);
        apiField.setType(type);
        apiField.setExample(example);
        apiField.setRequired(required);
        
        // Use setApiDefinition instead of setApiDefinitionAndLink
        apiField.setApiDefinition(apiDefinition);
        
        // Also add the field to the API definition's fields list
        apiDefinition.getFields().add(apiField);
        
        return apiFieldRepository.save(apiField);
    }

    // READ: Retrieve all ApiFields for a given apiDefinitionId
    public List<ApiField> getFieldsByApiDefinition(Long apiDefinitionId) {
        return apiFieldRepository.findByApiDefinitionId(apiDefinitionId);
    }

    // READ: Retrieve ApiField by ID
    public ApiField getApiFieldById(Long id) {
        return apiFieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("API Field not found"));
    }

    // UPDATE: Update an existing ApiField
    public ApiField updateApiField(Long id, ApiField updatedField) {
        ApiField apiField = getApiFieldById(id);
        apiField.setName(updatedField.getName());
        apiField.setType(updatedField.getType());
        apiField.setExample(updatedField.getExample());
        apiField.setRequired(updatedField.isRequired());

        return apiFieldRepository.save(apiField);
    }

    // DELETE: Delete ApiField by ID
    public boolean deleteApiField(Long id) {
        ApiField apiField = getApiFieldById(id);
        // Remove the field from the API definition's fields list
        APIDefinition apiDefinition = apiField.getApiDefinition();
        if (apiDefinition != null) {
            apiDefinition.getFields().remove(apiField);
        }
        apiFieldRepository.delete(apiField);
        return true;
    }
}