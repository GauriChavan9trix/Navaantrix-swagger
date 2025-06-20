package com.example.demo.service;

import com.example.demo.model.ApiParam;
import com.example.demo.repositary.APIDifinitionRepositary;
import com.example.demo.repositary.ApiParamRepository;
import com.example.demo.model.APIDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiParamService {

    private final ApiParamRepository apiParamRepository;
    private final APIDifinitionRepositary apiDefinitionRepository;

    @Autowired
    public ApiParamService(ApiParamRepository apiParamRepository, APIDifinitionRepositary apiDefinitionRepository) {
        this.apiParamRepository = apiParamRepository;
        this.apiDefinitionRepository = apiDefinitionRepository;
    }

    // CREATE: Create a new ApiParam
    public ApiParam createApiParam(Long apiDefinitionId, String name, String paramIn, String type, String example, boolean required) {
        APIDefinition apiDefinition = apiDefinitionRepository.findById(apiDefinitionId)
                .orElseThrow(() -> new RuntimeException("API Definition not found"));

        ApiParam apiParam = new ApiParam();
        apiParam.setName(name);
        apiParam.setParamIn(paramIn);
        apiParam.setType(type);
        apiParam.setExample(example);
        apiParam.setRequired(required);

        apiParam.setApiDefinitionAndLink(apiDefinition);
        return apiParamRepository.save(apiParam);
    }

    // READ: Retrieve all ApiParams for a given apiDefinitionId
    public List<ApiParam> getParamsByApiDefinition(Long apiDefinitionId) {
        return apiParamRepository.findByApiDefinitionId(apiDefinitionId);
    }

    // READ: Retrieve ApiParam by ID
    public ApiParam getApiParamById(Long id) {
        return apiParamRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("API Param not found"));
    }

    // UPDATE: Update an existing ApiParam
    public ApiParam updateApiParam(Long id, ApiParam updatedParam) {
        ApiParam apiParam = getApiParamById(id);
        apiParam.setName(updatedParam.getName());
        apiParam.setParamIn(updatedParam.getParamIn());
        apiParam.setType(updatedParam.getType());
        apiParam.setExample(updatedParam.getExample());
        apiParam.setRequired(updatedParam.isRequired());

        return apiParamRepository.save(apiParam);
    }

    // DELETE: Delete ApiParam by ID
    public boolean deleteApiParam(Long id) {
        ApiParam apiParam = getApiParamById(id);
        apiParamRepository.delete(apiParam);
        return true;
    }
}
