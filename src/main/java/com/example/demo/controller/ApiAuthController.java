package com.example.demo.controller;

import com.example.demo.model.ApiAuth;
import com.example.demo.service.ApiAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiAuthController {

    @Autowired
    private ApiAuthService apiAuthService;

    @PostMapping("/save")
    public ResponseEntity<ApiAuth> createAuth(@RequestParam Long apiDefinitionId,
                                              @RequestParam String type,
                                              @RequestParam(required = false) String username,
                                              @RequestParam(required = false) String password,
                                              @RequestParam(required = false) String token,
                                              @RequestParam(required = false) String apiKeyName,
                                              @RequestParam(required = false) String apiKeyValue) {
        ApiAuth auth = apiAuthService.createApiAuth(apiDefinitionId, type, username, password, token, apiKeyName, apiKeyValue);
        return ResponseEntity.ok(auth);
    }

    @GetMapping("/view/{apiDefinitionId}")
    public ResponseEntity<ApiAuth> getAuthByApiDefinition(@PathVariable Long apiDefinitionId) {
        ApiAuth auth = apiAuthService.getAuthByApiDefinition(apiDefinitionId);
        return ResponseEntity.ok(auth);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiAuth> updateAuth(@PathVariable Long id,
                                              @RequestBody ApiAuth updatedAuth) {
        ApiAuth auth = apiAuthService.updateApiAuth(id, updatedAuth);
        if (auth != null) {
            return ResponseEntity.ok(auth);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAuth(@PathVariable Long id) {
        boolean deleted = apiAuthService.deleteApiAuth(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
