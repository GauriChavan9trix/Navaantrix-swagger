package com.example.demo.controller;

import com.example.demo.model.Project;
import com.example.demo.repositary.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
@CrossOrigin(origins = "http://localhost:3000")
public class ProjectController {

    @Autowired
    private ProjectRepository projectRepository;

    // ✅ Create a new project
    @PostMapping("/create")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project savedProject = projectRepository.save(project);
        return ResponseEntity.ok(savedProject);
    }

    // ✅ Get all projects
    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjects() {
        try {
            List<Project> projects = projectRepository.findAll();
            System.out.println("Fetched projects: " + projects.size());
            return ResponseEntity.ok(projects);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    // ✅ Get project by ID
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable Long id) {
        return projectRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Update project
    @PutMapping(value = "/update/{id}", consumes = "application/json")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project updatedProject) {
        return projectRepository.findById(id)
                .map(project -> {
                    project.setName(updatedProject.getName());
                    project.setDescription(updatedProject.getDescription());
                    project.setBasePath(updatedProject.getBasePath());
                    return ResponseEntity.ok(projectRepository.save(project));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ Delete project
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // ✅ NEW: Update Bearer Token and expiry
  //  @PutMapping("/update-token/{projectId}")
   /* public ResponseEntity<Project> updateBearerToken(@PathVariable Long projectId,
                                                     @RequestParam String bearerToken) {
        return projectRepository.findById(projectId)
                .map(project -> {
                    project.setBearerToken(bearerToken);
                    project.setAuthType("Bearer");
                    project.setExpiresAt(LocalDateTime.now().plusHours(3)); // Adjust as needed
                    return ResponseEntity.ok(projectRepository.save(project));
                })
                .orElse(ResponseEntity.notFound().build());
    }*/
}
