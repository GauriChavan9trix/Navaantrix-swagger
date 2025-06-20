package com.example.demo.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


//http://localhost:5555/swagger-ui/swagger-ui/index.html?url=/swagger/api/definition/project/6/openapi.yaml

@CrossOrigin(origins = "http://localhost:3000") 
@Controller
@RequestMapping("/swagger-ui")

public class SwaggerUIController {

	@GetMapping("/{projectId}")
	public String getSwaggerUI(@PathVariable String projectId) {
	    return "redirect:/swagger-ui/index.html?url=/api/definition/project/" + projectId + "/openapi.yaml";
	}
	
/*	@GetMapping("/{projectId}")
	public String getSwaggerUI(@PathVariable String projectId) {
	    // Use URL encoding for the parameter
	    String encodedUrl = URLEncoder.encode("/api/definition/project/" + projectId + "/openapi.yaml", StandardCharsets.UTF_8);
	    return "redirect:/swagger-ui/index.html?url=" + encodedUrl;
	}*/


    @GetMapping
    public String redirectToSwaggerUI() {
        return "redirect:/swagger-ui/index.html";
    }
}
