package com.oziel.os.controller;
import com.oziel.os.dto.BuildLogDto;
import com.oziel.os.entity.LogCategory;
import com.oziel.os.service.LogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/v1/logs") @RequiredArgsConstructor
public class LogController {
    private final LogService service;
    @GetMapping public List<BuildLogDto> all(@RequestParam(required=false) LogCategory category){return service.findAll(category);}
    @PostMapping public ResponseEntity<BuildLogDto> create(@RequestBody BuildLogDto dto){return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto));}
}
