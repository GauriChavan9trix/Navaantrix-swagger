package com.example.demo.service;

import com.example.demo.YamlGenerator.YamlGenerator;
import com.example.demo.model.*;
import com.example.demo.repositary.APIDifinitionRepositary;
import com.example.demo.repositary.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ApiRegistryService {

    @Autowired
    private APIDifinitionRepositary apiDefinitionRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private YamlGenerator yamlGenerator;

    @Autowired
    public ApiRegistryService(
            ProjectRepository projectRepository,
            APIDifinitionRepositary apiDefinitionRepository,
            YamlGenerator yamlGenerator
    ) {
        this.projectRepository = projectRepository;
        this.apiDefinitionRepository = apiDefinitionRepository;
        this.yamlGenerator = yamlGenerator;
    }

    public APIDefinition saveApiDefinition(APIDefinition apiDefinition) throws IOException {
        validateApiDefinition(apiDefinition);
        validateAndSetFullPath(apiDefinition);
        APIDefinition saved = apiDefinitionRepository.save(apiDefinition);
        Project project = saved.getProject();
        List<APIDefinition> apis = apiDefinitionRepository.findByProjectId(project.getId());
        yamlGenerator.generateYamlFile(project, apis);
        return saved;
    }

    public List<APIDefinition> getAllApiDefinitions() {
        return apiDefinitionRepository.findAll();
    }

    public APIDefinition updateApiDefinition(Long id, APIDefinition updatedDefinition) throws IOException {
        Optional<APIDefinition> existingOpt = apiDefinitionRepository.findById(id);
        if (existingOpt.isEmpty()) {
            return null;
        }

        APIDefinition existing = existingOpt.get();
        existing.setName(updatedDefinition.getName());
        existing.setDescription(updatedDefinition.getDescription());
        existing.setMethod(updatedDefinition.getMethod());
        existing.setRelativePath(updatedDefinition.getRelativePath());
        existing.setSummary(updatedDefinition.getSummary());
        existing.setSchemaName(updatedDefinition.getSchemaName());

        // Handle fields
        if (updatedDefinition.getFields() != null) {
            existing.getFields().clear();
            existing.getFields().addAll(updatedDefinition.getFields());
            existing.getFields().forEach(f -> f.setApiDefinition(existing));
        }

        // Handle headers
        if (updatedDefinition.getHeaders() != null) {
            existing.getHeaders().clear();
            existing.getHeaders().addAll(updatedDefinition.getHeaders());
            existing.getHeaders().forEach(h -> h.setApiDefinition(existing));
        }

        // Handle params
        if (updatedDefinition.getParams() != null) {
            existing.getParams().clear();
            existing.getParams().addAll(updatedDefinition.getParams());
            existing.getParams().forEach(p -> p.setApiDefinition(existing));
        }

        // Handle auth
        if (updatedDefinition.getAuth() != null) {
            if (existing.getAuth() == null) {
                existing.setAuth(updatedDefinition.getAuth());
                existing.getAuth().setApiDefinition(existing);
            } else {
                existing.getAuth().setType(updatedDefinition.getAuth().getType());
                existing.getAuth().setUsername(updatedDefinition.getAuth().getUsername());
                existing.getAuth().setPassword(updatedDefinition.getAuth().getPassword());
                existing.getAuth().setToken(updatedDefinition.getAuth().getToken());
                existing.getAuth().setApiKeyName(updatedDefinition.getAuth().getApiKeyName());
                existing.getAuth().setApiKeyValue(updatedDefinition.getAuth().getApiKeyValue());
            }
        }

        validateApiDefinition(existing);
        validateAndSetFullPath(existing);
        APIDefinition updated = apiDefinitionRepository.save(existing);
        Project project = updated.getProject();
        List<APIDefinition> apis = apiDefinitionRepository.findByProjectId(project.getId());
        yamlGenerator.generateYamlFile(project, apis);
        return updated;
    }

    public boolean deleteApiDefinition(Long id) throws IOException {
        if (!apiDefinitionRepository.existsById(id)) {
            return false;
        }
        APIDefinition apiDefinition = apiDefinitionRepository.findById(id).get();
        Project project = apiDefinition.getProject();
        apiDefinitionRepository.deleteById(id);
        List<APIDefinition> apis = apiDefinitionRepository.findByProjectId(project.getId());
        yamlGenerator.generateYamlFile(project, apis);
        return true;
    }

    public List<APIDefinition> getApisByProjectId(Long projectId) {
        return apiDefinitionRepository.findByProjectId(projectId);
    }

    private void validateAndSetFullPath(APIDefinition apiDefinition) {
        if (apiDefinition.getProject() == null || apiDefinition.getProject().getId() == null) {
            throw new IllegalArgumentException("API definition must be associated with a project");
        }

        Project project = projectRepository.findById(apiDefinition.getProject().getId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));
        apiDefinition.setProject(project);

        String basePath = (project.getBasePath() != null) ? project.getBasePath() : "";
        String endpoint = (apiDefinition.getRelativePath() != null) ? apiDefinition.getRelativePath() : "";

        basePath = basePath.endsWith("/") ? basePath.substring(0, basePath.length() - 1) : basePath;
        endpoint = endpoint.startsWith("/") ? endpoint : "/" + endpoint;

        apiDefinition.setFullPath(basePath + endpoint);
    }

    public File generateOpenApiYamlForProject(Long projectId) {
        try {
            List<APIDefinition> apis = apiDefinitionRepository.findByProjectId(projectId);
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new RuntimeException("Project not found"));

            String fileName = "D:/NavaantrixSwagger/generated/" + projectId + ".yaml";
            Path path = Paths.get(fileName);
            Files.createDirectories(path.getParent());

            String content = yamlGenerator.generateYamlContentForProject(project, apis);
            Files.write(path, content.getBytes());
            return path.toFile();
        } catch (Exception e) {
            throw new RuntimeException("Error generating YAML", e);
        }
    }

    public String generateOpenApiYamlContent(Long projectId) {
        try {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new RuntimeException("Project not found"));
            List<APIDefinition> apis = apiDefinitionRepository.findByProjectId(projectId);
            String yamlContent = yamlGenerator.generateYamlContentForProject(project, apis);

            // ✅ Save the file as well
            String filePath = "D:/NavaantrixProjects/NavaantrixSwagger/generated/" + projectId + ".yaml";
            Files.createDirectories(Paths.get("D:/NavaantrixProjects/NavaantrixSwagger/generated/"));

            try (FileWriter writer = new FileWriter(filePath)) {
                writer.write(yamlContent);
            }

            return yamlContent;
        } catch (IOException e) {
            throw new RuntimeException("Error saving YAML file", e);
        }
    }

    // ✅ Validation Methods

    private void validateApiDefinition(APIDefinition api) {
        List<String> errors = new ArrayList<>();

        // Path
        if (api.getRelativePath() == null || !api.getRelativePath().startsWith("/")) {
            errors.add("Relative path must start with '/'");
        }

        // Method
        List<String> allowedMethods = Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH");
        if (api.getMethod() == null || !allowedMethods.contains(api.getMethod().toUpperCase())) {
            errors.add("HTTP method must be one of GET, POST, PUT, DELETE, PATCH");
        }

        // Fields
        if (api.getFields() != null) {
            Set<String> fieldNames = new HashSet<>();
            for (ApiField field : api.getFields()) {
                if (!fieldNames.add(field.getName())) {
                    errors.add("Duplicate field name: " + field.getName());
                }
                if (!isValidType(field.getType())) {
                    errors.add("Invalid type for field: " + field.getName());
                }
            }
        }

        // Path param check
        List<String> pathVars = extractPathVars(api.getRelativePath());
        List<String> definedPathVars = api.getParams().stream()
                .filter(p -> "path".equalsIgnoreCase(p.getParamIn()))
                .map(ApiParam::getName)
                .collect(Collectors.toList());

        for (String var : pathVars) {
            if (!definedPathVars.contains(var)) {
                errors.add("Missing path param definition for: " + var);
            }
        }

        for (String def : definedPathVars) {
            if (!pathVars.contains(def)) {
                errors.add("Defined path param not found in URL: " + def);
            }
        }

        if (!errors.isEmpty()) {
            throw new IllegalArgumentException("API validation failed:\n" + String.join("\n", errors));
        }
    }

    private List<String> extractPathVars(String path) {
        Matcher matcher = Pattern.compile("\\{([^}]+)}").matcher(path);
        List<String> vars = new ArrayList<>();
        while (matcher.find()) {
            vars.add(matcher.group(1));
        }
        return vars;
    }

    private boolean isValidType(String type) {
        return Arrays.asList("string", "integer", "number", "boolean", "array", "object").contains(type.toLowerCase());
    }
}
