package com.example.demo.YamlGenerator;

import com.example.demo.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class YamlGenerator {

    private static final Logger log = LoggerFactory.getLogger(YamlGenerator.class);

    public void generateYamlFile(Project project, List<APIDefinition> apiDefinitions) throws IOException {
        String yamlContent = generateYamlContentForProject(project, apiDefinitions);
        String filePath = "D:/NavaantrixProjects/NavaantrixSwagger/generated/" + project.getId() + ".yaml";
        Files.createDirectories(Paths.get("D:/NavaantrixProjects/NavaantrixSwagger/generated/"));

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(yamlContent);
        }

        log.info("✅ YAML file generated at '{}'", filePath);
    }

    public String generateYamlContentForProject(Project project, List<APIDefinition> apis) {
        Map<String, Object> openApiSpec = new LinkedHashMap<>();
        openApiSpec.put("openapi", "3.0.0");

        // Info section
        Map<String, Object> info = new LinkedHashMap<>();
        info.put("title", project.getName() + " API");
        info.put("version", "1.0.0");
        info.put("description", project.getDescription());
        openApiSpec.put("info", info);

        // Servers section
        List<Map<String, String>> servers = new ArrayList<>();
        Map<String, String> server = new LinkedHashMap<>();
        server.put("url", project.getBasePath());
        if (project.getDescription() != null) {
            server.put("description", project.getDescription());
        }
        servers.add(server);
        openApiSpec.put("servers", servers);

        // Paths section
        Map<String, Object> paths = new LinkedHashMap<>();
        Map<String, List<APIDefinition>> apisByPath = apis.stream()
            .filter(api -> api.getMethod() != null && !api.getMethod().isEmpty())
            .collect(Collectors.groupingBy(APIDefinition::getRelativePath));

        for (Map.Entry<String, List<APIDefinition>> entry : apisByPath.entrySet()) {
            String path = entry.getKey();
            Map<String, Object> pathMethods = new LinkedHashMap<>();

            for (APIDefinition api : entry.getValue()) {
                if (api.getMethod() == null || api.getMethod().isEmpty()) {
                    log.warn("⚠️ Skipping API with null method: {}", api.getRelativePath());
                    continue;
                }

                Map<String, Object> methodDetails = new LinkedHashMap<>();
                methodDetails.put("summary", api.getSummary());

                if (api.getDescription() != null && !api.getDescription().isEmpty()) {
                    methodDetails.put("description", api.getDescription());
                }

                // Parameters
                if (api.getParams() != null && !api.getParams().isEmpty()) {
                    methodDetails.put("parameters", createParameters(api.getParams()));
                }

                // Headers
                if (api.getHeaders() != null && !api.getHeaders().isEmpty()) {
                    List<Map<String, Object>> existingParams = (List<Map<String, Object>>)
                        methodDetails.getOrDefault("parameters", new ArrayList<>());
                    existingParams.addAll(createHeaders(api.getHeaders()));
                    methodDetails.put("parameters", existingParams);
                }

                // Request body
                if (api.getFields() != null && !api.getFields().isEmpty()) {
                    methodDetails.put("requestBody", createRequestBody(api));
                }

                // Responses
                Map<String, Object> responses = new LinkedHashMap<>();
                responses.put("200", Map.of("description", "Successful operation"));
                methodDetails.put("responses", responses);

                // Security
                if (api.getAuth() != null && !"none".equalsIgnoreCase(api.getAuth().getType())) {
                    methodDetails.put("security", createSecurity(api));
                }

                pathMethods.put(api.getMethod().toLowerCase(), methodDetails);
            }

            paths.put(path, pathMethods);
        }

        openApiSpec.put("paths", paths);

        // Components section
        Map<String, Object> components = new LinkedHashMap<>();

        // Security schemes
        Map<String, Object> securitySchemes = new LinkedHashMap<>();
        for (APIDefinition api : apis) {
            if (api.getAuth() != null && !"none".equalsIgnoreCase(api.getAuth().getType())) {
                securitySchemes.put("auth_" + api.getId(), createSecurityScheme(api.getAuth()));
            }
        }
        if (!securitySchemes.isEmpty()) {
            components.put("securitySchemes", securitySchemes);
        }

        // Schemas
        Map<String, Object> schemas = new LinkedHashMap<>();
        for (APIDefinition api : apis) {
            if (api.getFields() != null && !api.getFields().isEmpty()) {
                schemas.put(getSchemaName(api), createSchema(api.getFields()));
            }
        }
        if (!schemas.isEmpty()) {
            components.put("schemas", schemas);
        }

        openApiSpec.put("components", components);

        // Dump YAML
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        String yamlContent = new Yaml(options).dump(openApiSpec);
        log.debug("Generated YAML for project {}:\n{}", project.getId(), yamlContent);

        return yamlContent;
    }

    private String getSchemaName(APIDefinition api) {
        String rawName = (api.getSchemaName() != null && !api.getSchemaName().isEmpty())
                ? api.getSchemaName()
                : "Schema_" + api.getId();

        return sanitizeSchemaName(rawName);
    }

    private String sanitizeSchemaName(String name) {
        if (name == null || name.isEmpty()) return "DefaultSchema";
        return name.replaceAll("[^A-Za-z0-9_.-]", "_");
    }

    private List<Map<String, Object>> createParameters(List<ApiParam> params) {
        return params.stream().map(param -> {
            Map<String, Object> paramMap = new LinkedHashMap<>();
            paramMap.put("name", param.getName());
            paramMap.put("in", param.getParamIn());
            paramMap.put("required", param.isRequired());

            Map<String, String> schema = new LinkedHashMap<>();
            schema.put("type", param.getType());
            paramMap.put("schema", schema);

            if (param.getExample() != null && !param.getExample().isEmpty()) {
                paramMap.put("example", param.getExample());
            }

            return paramMap;
        }).collect(Collectors.toList());
    }

    private List<Map<String, Object>> createHeaders(List<ApiHeader> headers) {
        return headers.stream().map(header -> {
            Map<String, Object> headerMap = new LinkedHashMap<>();
            headerMap.put("name", header.getName());
            headerMap.put("in", "header");
            headerMap.put("required", true);

            Map<String, String> schema = new LinkedHashMap<>();
            schema.put("type", "string");
            headerMap.put("schema", schema);

            if (header.getValue() != null && !header.getValue().isEmpty()) {
                headerMap.put("example", header.getValue());
            }

            return headerMap;
        }).collect(Collectors.toList());
    }

    private Map<String, Object> createRequestBody(APIDefinition api) {
        Map<String, Object> requestBody = new LinkedHashMap<>();
        requestBody.put("description", "Request payload");
        requestBody.put("required", true);

        Map<String, Object> content = new LinkedHashMap<>();
        Map<String, Object> mediaType = new LinkedHashMap<>();

        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("$ref", "#/components/schemas/" + getSchemaName(api));
        mediaType.put("schema", schema);

        content.put("application/json", mediaType);
        requestBody.put("content", content);

        return requestBody;
    }

    private Map<String, Object> createSchema(List<ApiField> fields) {
        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "object");

        Map<String, Object> properties = new LinkedHashMap<>();
        List<String> requiredFields = new ArrayList<>();

        for (ApiField field : fields) {
            if (field.getName() == null || !field.getName().matches("^[a-zA-Z_][a-zA-Z0-9_]*$")) {
                log.warn("⚠️ Invalid field name '{}' - skipping field", field.getName());
                continue;
            }

            Map<String, Object> fieldProps = new LinkedHashMap<>();
            fieldProps.put("type", field.getType());
            fieldProps.put("description", Optional.ofNullable(field.getDescription()).orElse(""));

            if (field.getExample() != null && !field.getExample().isEmpty()) {
                fieldProps.put("example", field.getExample());
            }

            properties.put(field.getName(), fieldProps);
            if (field.isRequired()) {
                requiredFields.add(field.getName());
            }
        }

        schema.put("properties", properties);
        if (!requiredFields.isEmpty()) {
            schema.put("required", requiredFields);
        }

        return schema;
    }

    private Map<String, Object> createSecurityScheme(ApiAuth auth) {
        Map<String, Object> scheme = new LinkedHashMap<>();
        String type = auth.getType().toLowerCase();

        switch (type) {
            case "basic":
                scheme.put("type", "http");
                scheme.put("scheme", "basic");
                break;
            case "bearer":
                scheme.put("type", "http");
                scheme.put("scheme", "bearer");
                scheme.put("bearerFormat", auth.getBearerFormat() != null ? auth.getBearerFormat() : "JWT");
                break;
            case "apikey":
                scheme.put("type", "apiKey");
                scheme.put("name", auth.getApiKeyName() != null ? auth.getApiKeyName() : "X-API-KEY");
                scheme.put("in", auth.getApiKeyLocation() != null ? auth.getApiKeyLocation() : "header");
                break;
            default:
                scheme.put("type", "http");
                scheme.put("scheme", "basic");
        }
        return scheme;
    }

    private List<Map<String, List<String>>> createSecurity(APIDefinition api) {
        Map<String, List<String>> securityRequirement = new LinkedHashMap<>();
        securityRequirement.put("auth_" + api.getId(), new ArrayList<>());
        return Collections.singletonList(securityRequirement);
    }
}
