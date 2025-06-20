package com.example.demo.service;

import com.example.demo.model.ApiAuth;
import com.example.demo.model.APIDefinition;
import com.example.demo.repositary.APIDifinitionRepositary;
import com.example.demo.repositary.ApiAuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ApiAuthService {

    private  ApiAuthRepository apiAuthRepository;
    private  APIDifinitionRepositary apiDefinitionRepository;

    @Autowired
    public ApiAuthService(ApiAuthRepository apiAuthRepository, APIDifinitionRepositary apiDefinitionRepository) {
        this.apiAuthRepository = apiAuthRepository;
        this.apiDefinitionRepository = apiDefinitionRepository;
    }

    // CREATE
    public ApiAuth createApiAuth(Long apiDefinitionId, String type, String username, String password, String token, String apiKeyName, String apiKeyValue) {
        APIDefinition apiDefinition = apiDefinitionRepository.findById(apiDefinitionId)
                .orElseThrow(() -> new RuntimeException("API Definition not found"));

        ApiAuth apiAuth = new ApiAuth();
        apiAuth.setType(type);
        apiAuth.setUsername(username);
        apiAuth.setPassword(password);
        apiAuth.setToken(token);
        apiAuth.setApiKeyName(apiKeyName);
        apiAuth.setApiKeyValue(apiKeyValue);

        apiAuth.setApiDefinitionAndLink(apiDefinition);
        return apiAuthRepository.save(apiAuth);
    }

    // READ by apiDefinitionId
    public ApiAuth getAuthByApiDefinition(Long apiDefinitionId) {
        return apiAuthRepository.findByApiDefinitionId(apiDefinitionId)
                .orElseThrow(() -> new RuntimeException("API Auth not found for API Definition ID"));
    }

    // UPDATE
    public ApiAuth updateApiAuth(Long id, ApiAuth updatedAuth) {
        ApiAuth apiAuth = apiAuthRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("API Auth not found"));

        apiAuth.setType(updatedAuth.getType());
        apiAuth.setUsername(updatedAuth.getUsername());
        apiAuth.setPassword(updatedAuth.getPassword());
        apiAuth.setToken(updatedAuth.getToken());
        apiAuth.setApiKeyName(updatedAuth.getApiKeyName());
        apiAuth.setApiKeyValue(updatedAuth.getApiKeyValue());

        return apiAuthRepository.save(apiAuth);
    }

    // DELETE
    public boolean deleteApiAuth(Long id) {
        ApiAuth apiAuth = apiAuthRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("API Auth not found"));
        apiAuthRepository.delete(apiAuth);
        return true;
    }
}
