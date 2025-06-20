package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "api_field")
public class ApiField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String example;
    private boolean required;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_id", nullable = false)
    @JsonIgnore
    private APIDefinition apiDefinition;

    public ApiField() {}

    public ApiField(String name, String type, String example, boolean required, String description) {
        this.name = name;
        this.type = type;
        this.example = example;
        this.required = required;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getExample() { return example; }
    public void setExample(String example) { this.example = example; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public APIDefinition getApiDefinition() { return apiDefinition; }
    public void setApiDefinition(APIDefinition apiDefinition) { this.apiDefinition = apiDefinition; }
}