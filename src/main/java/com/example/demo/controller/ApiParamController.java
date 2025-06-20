package com.example.demo.controller;

import com.example.demo.model.ApiParam;
import com.example.demo.service.ApiParamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/param")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiParamController {

    @Autowired
    private ApiParamService apiParamService;

    @PostMapping("/save")
    public ResponseEntity<ApiParam> createParam(@RequestParam Long apiDefinitionId,
                                                @RequestParam String name,
                                                @RequestParam String paramIn,
                                                @RequestParam String type,
                                                @RequestParam String example,
                                                @RequestParam boolean required) {
        ApiParam param = apiParamService.createApiParam(apiDefinitionId, name, paramIn, type, example, required);
        return ResponseEntity.ok(param);
    }

    @GetMapping("/view/{apiDefinitionId}")
    public ResponseEntity<List<ApiParam>> getParamsByApiDefinition(@PathVariable Long apiDefinitionId) {
        List<ApiParam> params = apiParamService.getParamsByApiDefinition(apiDefinitionId);
        return ResponseEntity.ok(params);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiParam> updateParam(@PathVariable Long id,
                                                @RequestBody ApiParam updatedParam) {
        ApiParam param = apiParamService.updateApiParam(id, updatedParam);
        if (param != null) {
            return ResponseEntity.ok(param);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteParam(@PathVariable Long id) {
        boolean deleted = apiParamService.deleteApiParam(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
