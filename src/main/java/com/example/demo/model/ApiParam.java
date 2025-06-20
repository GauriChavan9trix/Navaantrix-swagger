package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "api_param")
public class ApiParam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;
    
    @Column(name = "param_in")  // Add this annotation
    private String paramIn; // query or path
    
    @Column(name = "type")
    private String type;
    
    @Column(name = "example")  // Optional but consistent
    private String example;
    
    @Column(name = "required")
    private boolean required;

    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_id", nullable = false)
    @JsonIgnore
    private APIDefinition apiDefinition;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getParamIn() { return paramIn; }
    public void setParamIn(String paramIn) { this.paramIn = paramIn; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getExample() { return example; }
    public void setExample(String example) { this.example = example; }

    public boolean isRequired() { return required; }
    public void setRequired(boolean required) { this.required = required; }

    public APIDefinition getApiDefinition() { return apiDefinition; }
    public void setApiDefinition(APIDefinition apiDefinition) { this.apiDefinition = apiDefinition; }

    // Helper method for bidirectional relationship
    public void setApiDefinitionAndLink(APIDefinition apiDefinition) {
        this.apiDefinition = apiDefinition;
        if (apiDefinition != null) {
            apiDefinition.addParam(this);
        }
    }
}
