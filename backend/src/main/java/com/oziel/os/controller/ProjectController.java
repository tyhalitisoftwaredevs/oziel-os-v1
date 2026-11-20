package com.oziel.os.controller;
import com.oziel.os.dto.ProjectDto;
import com.oziel.os.entity.ProjectStatus;
import com.oziel.os.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/v1/projects") @RequiredArgsConstructor
public class ProjectController {
    private final ProjectService service;
    @GetMapping public List<ProjectDto> all(@RequestParam(required=false) ProjectStatus status,@RequestParam(required=false) Boolean featured){return service.findAll(status,featured);}
    @GetMapping("/{slug}") public ProjectDto one(@PathVariable String slug){return service.findBySlug(slug);}
    @PostMapping public ResponseEntity<ProjectDto> create(@Valid @RequestBody ProjectDto dto){return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));}
    @PutMapping("/{id}") public ProjectDto update(@PathVariable Long id,@Valid @RequestBody ProjectDto dto){return service.update(id,dto);}
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){service.delete(id);return ResponseEntity.noContent().build();}
}
