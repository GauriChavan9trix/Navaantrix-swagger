package com.example.demo.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "api_info")
public class APIDefinition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private String method;
    private String summary;
    
    @Column(name = "schema_name")
    private String schemaName;

    @Column(name = "relative_path")
    private String relativePath;

    @Column(name = "full_path")
    private String fullPath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonBackReference
    private Project project;

    @OneToMany(mappedBy = "apiDefinition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApiField> fields;

    @OneToMany(mappedBy = "apiDefinition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApiHeader> headers;

    @OneToMany(mappedBy = "apiDefinition", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ApiParam> params;

    @OneToOne(mappedBy = "apiDefinition", cascade = CascadeType.ALL, orphanRemoval = true)
    private ApiAuth auth;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }
    public String getRelativePath() { return relativePath; }
    public void setRelativePath(String relativePath) { this.relativePath = relativePath; }
    public String getFullPath() { return fullPath; }
    public void setFullPath(String fullPath) { this.fullPath = fullPath; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    public List<ApiField> getFields() { return fields; }
    public void setFields(List<ApiField> fields) { this.fields = fields; }
    public List<ApiHeader> getHeaders() { return headers; }
    public void setHeaders(List<ApiHeader> headers) { this.headers = headers; }
    public List<ApiParam> getParams() { return params; }
    public void setParams(List<ApiParam> params) { this.params = params; }
    public ApiAuth getAuth() { return auth; }
    public void setAuth(ApiAuth auth) { 
        this.auth = auth;
        if (auth != null) {
            auth.setApiDefinition(this);
        }
    }
    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }
    public String getSchemaName() { return schemaName; }
    public void setSchemaName(String schemaName) { this.schemaName = schemaName; }

    // Convenience methods for bidirectional relationships
    public void addField(ApiField field) {
        fields.add(field);
        field.setApiDefinition(this);
    }

    public void addHeader(ApiHeader header) {
        headers.add(header);
        header.setApiDefinition(this);
    }

    public void addParam(ApiParam param) {
        params.add(param);
        param.setApiDefinition(this);
    }

    // Method to set all bidirectional relationships
    public void setRelationships() {
        if (fields != null) {
            fields.forEach(field -> field.setApiDefinition(this));
        }
        if (headers != null) {
            headers.forEach(header -> header.setApiDefinition(this));
        }
        if (params != null) {
            params.forEach(param -> param.setApiDefinition(this));
        }
        if (auth != null) {
            auth.setApiDefinition(this);
        }
    }
}