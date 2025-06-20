package com.example.demo.controller;

import com.example.demo.model.ApiHeader;
import com.example.demo.service.ApiHeaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/header")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiHeaderController {

    @Autowired
    private ApiHeaderService apiHeaderService;

    // 1️⃣ CREATE HEADER
    @PostMapping("/save")
    public ResponseEntity<ApiHeader> createHeader(@RequestParam Long apiDefinitionId,
                                                  @RequestParam String name,
                                                  @RequestParam String value) {
        ApiHeader header = apiHeaderService.createApiHeader(apiDefinitionId, name, value);
        return ResponseEntity.ok(header);
    }

    // 2️⃣ GET HEADERS BY API DEFINITION ID
    @GetMapping("/view/{apiDefinitionId}")
    public ResponseEntity<List<ApiHeader>> getHeadersByApiDefinition(@PathVariable Long apiDefinitionId) {
        List<ApiHeader> headers = apiHeaderService.getHeadersByApiDefinition(apiDefinitionId);
        return ResponseEntity.ok(headers);
    }

    // 3️⃣ UPDATE HEADER BY ID
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiHeader> updateHeader(@PathVariable Long id,
                                                  @RequestBody ApiHeader updatedHeader) {
        try {
            ApiHeader header = apiHeaderService.updateApiHeader(id, updatedHeader);
            return ResponseEntity.ok(header);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 4️⃣ DELETE HEADER BY ID
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteHeader(@PathVariable Long id) {
        boolean deleted = apiHeaderService.deleteApiHeader(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
