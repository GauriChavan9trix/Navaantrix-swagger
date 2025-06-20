package com.example.demo.service;

import com.example.demo.model.APIDefinition;
import com.example.demo.model.ApiHeader;
import com.example.demo.repositary.APIDifinitionRepositary;
import com.example.demo.repositary.ApiHeaderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiHeaderService {

    @Autowired
    private ApiHeaderRepository apiHeaderRepository;

    @Autowired
    private APIDifinitionRepositary apiDefinitionRepository;

    // 1️⃣ CREATE HEADER
    public ApiHeader createApiHeader(Long apiDefinitionId, String name, String value) {
        APIDefinition apiDefinition = apiDefinitionRepository.findById(apiDefinitionId)
            .orElseThrow(() -> new RuntimeException("API Definition not found with id: " + apiDefinitionId));

        ApiHeader header = new ApiHeader();
        header.setName(name);
        header.setValue(value);

        apiDefinition.addHeader(header);
        apiDefinitionRepository.save(apiDefinition);

        return header;
    }

    // 2️⃣ READ HEADERS (GET ALL HEADERS FOR API DEFINITION)
    public List<ApiHeader> getHeadersByApiDefinition(Long apiDefinitionId) {
        return apiHeaderRepository.findByApiDefinitionId(apiDefinitionId);
    }

    // 3️⃣ UPDATE HEADER (BY ID)
    public ApiHeader updateApiHeader(Long id, ApiHeader updatedHeader) {
        return apiHeaderRepository.findById(id).map(header -> {
            header.setName(updatedHeader.getName());
            header.setValue(updatedHeader.getValue());
            return apiHeaderRepository.save(header);
        }).orElseThrow(() -> new RuntimeException("ApiHeader not found with id: " + id));
    }

    // 4️⃣ DELETE HEADER (BY ID)
    public boolean deleteApiHeader(Long id) {
        if (apiHeaderRepository.existsById(id)) {
            apiHeaderRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
