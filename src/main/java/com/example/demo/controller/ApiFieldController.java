package com.example.demo.controller;

import com.example.demo.model.ApiField;
import com.example.demo.service.ApiFieldService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/field")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiFieldController {

    @Autowired
    private ApiFieldService apiFieldService;

    @PostMapping("/save")
    public ResponseEntity<ApiField> createField(@RequestParam Long apiDefinitionId,
                                                @RequestParam String name,
                                                @RequestParam String type,
                                                @RequestParam String example,
                                                @RequestParam boolean required) {
        ApiField field = apiFieldService.createApiField(apiDefinitionId, name, type, example, required);
        return ResponseEntity.ok(field);
    }

    @GetMapping("/view/{apiDefinitionId}")
    public ResponseEntity<List<ApiField>> getFieldsByApiDefinition(@PathVariable Long apiDefinitionId) {
        List<ApiField> fields = apiFieldService.getFieldsByApiDefinition(apiDefinitionId);
        return ResponseEntity.ok(fields);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiField> updateField(@PathVariable Long id,
                                                @RequestBody ApiField updatedField) {
        ApiField field = apiFieldService.updateApiField(id, updatedField);
        if (field != null) {
            return ResponseEntity.ok(field);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteField(@PathVariable Long id) {
        boolean deleted = apiFieldService.deleteApiField(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
