package com.example.demo.controller;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/swagger")
@CrossOrigin(origins = "http://localhost:3000")
public class SwaggerDocController {

    @GetMapping(value = "/{projectId}.yaml", produces = "application/yaml")
    public ResponseEntity<Resource> getSwaggerYaml(@PathVariable String projectId) {
        try {
            Path yamlPath = Paths.get("D:/NavaantrixProjects/NavaantrixSwagger/generated/" + projectId + ".yaml"); // ✅ Correct path
            Resource resource = new FileSystemResource(yamlPath);

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + projectId + ".yaml\"")
                    .contentType(MediaType.parseMediaType("application/yaml"))
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
