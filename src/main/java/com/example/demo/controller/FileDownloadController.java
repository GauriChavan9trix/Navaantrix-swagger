package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.service.ApiRegistryService;

//http://localhost:5555/api/definition/project/6/openapi.yaml

@RestController
public class FileDownloadController {

    @Autowired
    private ApiRegistryService apiRegistryService;

    @GetMapping(
        value = "/api/definition/project/{projectId}/openapi.yaml", 
        produces = "application/yaml"
    )
    public ResponseEntity<String> getOpenApiYaml(@PathVariable Long projectId) {
        try {
            // Corrected method name
            String yamlContent = apiRegistryService.generateOpenApiYamlContent(projectId);
            
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/x-yaml"))
                .header("Content-Disposition", "inline; filename=\"openapi.yaml\"")
                .body(yamlContent);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                .body("Error generating OpenAPI YAML: " + e.getMessage());
        }
    }/*
    @GetMapping(
    	    value = "/api/definition/project/{projectId}/openapi.yaml", 
    	    produces = "application/yaml"
    	)
    	public ResponseEntity<String> getOpenApiYaml(@PathVariable Long projectId) {
    	    try {
    	        String yamlContent = apiRegistryService.generateOpenApiYamlContent(projectId);
    	        
    	        return ResponseEntity.ok()
    	            .contentType(MediaType.parseMediaType("application/yaml")) // Fixed content type
    	            .header("Content-Disposition", "inline; filename=\"openapi.yaml\"")
    	            .body(yamlContent);
    	    } catch (Exception e) {
    	        return ResponseEntity.internalServerError()
    	            .body("Error generating OpenAPI YAML: " + e.getMessage());
    	    }
    	}*/
}