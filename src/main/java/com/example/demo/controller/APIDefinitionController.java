package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.YamlGenerator.YamlGenerator;
import com.example.demo.model.APIDefinition;
import com.example.demo.model.Project;
import com.example.demo.repositary.ProjectRepository;
import com.example.demo.service.ApiRegistryService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/definition")
@CrossOrigin(origins = "http://localhost:3000")
public class APIDefinitionController {

    private final ApiRegistryService apiRegistryService;
    private final ProjectRepository projectRepository;
    private final YamlGenerator yamlGenerator;
    @Autowired
    public APIDefinitionController(ApiRegistryService apiRegistryService, 
                                 ProjectRepository projectRepository, YamlGenerator yamlGenerator) {
        this.apiRegistryService = apiRegistryService;
        this.projectRepository = projectRepository;
		this.yamlGenerator = yamlGenerator;
    }

    @PostMapping("/save")
    public ResponseEntity<APIDefinition> createApiDefinition(
            @RequestBody APIDefinition apiDefinition, 
            @RequestHeader("Content-Type") String contentType) throws IOException {
        
        // Validate content type
        if (!MediaType.APPLICATION_JSON_VALUE.equals(contentType)) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build();
        }
        
        // Validate project
        if (apiDefinition.getProject() == null || apiDefinition.getProject().getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Project project = projectRepository.findById(apiDefinition.getProject().getId())
            .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        apiDefinition.setProject(project);
        
        // Set bidirectional relationships
        if (apiDefinition.getFields() != null) {
            apiDefinition.getFields().forEach(field -> field.setApiDefinition(apiDefinition));
        }
        if (apiDefinition.getHeaders() != null) {
            apiDefinition.getHeaders().forEach(header -> header.setApiDefinition(apiDefinition));
        }
        if (apiDefinition.getParams() != null) {
            apiDefinition.getParams().forEach(param -> param.setApiDefinition(apiDefinition));
        }
        if (apiDefinition.getAuth() != null) {
            apiDefinition.getAuth().setApiDefinition(apiDefinition);
        }
        APIDefinition saved = apiRegistryService.saveApiDefinition(apiDefinition);
        
        // Generate YAML file after saving the API definition
        List<APIDefinition> apiDefinitions = apiRegistryService.getApisByProjectId(project.getId());
        yamlGenerator.generateYamlFile(project, apiDefinitions);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/view")
    public List<APIDefinition> getAllDefinitions() {
        return apiRegistryService.getAllApiDefinitions();
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<APIDefinition> updateApiDefinition(
            @PathVariable Long id, 
            @RequestBody APIDefinition apiDefinition) throws IOException {
        APIDefinition updated = apiRegistryService.updateApiDefinition(id, apiDefinition);
        return updated != null 
                ? ResponseEntity.ok(updated) 
                : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteApiDefinition(@PathVariable Long id) throws IOException {
        return apiRegistryService.deleteApiDefinition(id) 
                ? ResponseEntity.noContent().build() 
                : ResponseEntity.notFound().build();
    }
    
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<APIDefinition>> getApisByProject(@PathVariable Long projectId) {
        List<APIDefinition> apis = apiRegistryService.getApisByProjectId(projectId);
        return ResponseEntity.ok(apis);
    }
}