package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "api_auth")
public class ApiAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type; // none, basic, bearer, apikey
    private String username;
    private String password;
    private String token;
    
    @Column(name = "api_key_name")  // Changed to match DB column
    private String apiKeyName;
    
    @Column(name = "api_key_value") // Changed to match DB column
    private String apiKeyValue;
    
    @Column(name = "bearer_format")
    private String bearerFormat;  // Maps to new column
    
    @Column(name = "api_key_location")
    private String apiKeyLocation; // Maps to new column

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "api_id")
    @JsonIgnore
    private APIDefinition apiDefinition;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getApiKeyName() { return apiKeyName; }
    public void setApiKeyName(String apiKeyName) { this.apiKeyName = apiKeyName; }

    public String getApiKeyValue() { return apiKeyValue; }
    public void setApiKeyValue(String apiKeyValue) { this.apiKeyValue = apiKeyValue; }

    public String getBearerFormat() { return bearerFormat; }
    public void setBearerFormat(String bearerFormat) { this.bearerFormat = bearerFormat; }

   public String getApiKeyLocation() { return apiKeyLocation; }
  public void setApiKeyLocation(String apiKeyLocation) { this.apiKeyLocation = apiKeyLocation; }

    public APIDefinition getApiDefinition() { return apiDefinition; }
    public void setApiDefinition(APIDefinition apiDefinition) { this.apiDefinition = apiDefinition; }

    // Helper method for bidirectional relationship
    public void setApiDefinitionAndLink(APIDefinition apiDefinition) {
        this.apiDefinition = apiDefinition;
        if (apiDefinition != null) {
            apiDefinition.setAuth(this);
        }
    }
}