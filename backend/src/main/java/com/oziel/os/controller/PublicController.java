package com.oziel.os.controller;
import com.oziel.os.dto.*;
import com.oziel.os.entity.OpportunityStatus;
import com.oziel.os.repository.ImpactStatRepository;
import com.oziel.os.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/v1") @RequiredArgsConstructor
public class PublicController {
    private final SkillService skills; private final OpportunityService opportunities;
    private final DashboardService dashboard; private final ImpactStatRepository impactStats;
    @GetMapping("/skills") public Map<?,?> skills(){return skills.findGrouped();}
    @GetMapping("/opportunities") public List<OpportunityDto> opportunities(){return opportunities.findAll();}
    @PostMapping("/opportunities") public OpportunityDto create(@RequestBody OpportunityDto dto){return opportunities.create(dto);}
    @PutMapping("/opportunities/{id}/status") public OpportunityDto status(@PathVariable Long id,@RequestBody Map<String,OpportunityStatus> body){return opportunities.updateStatus(id,body.get("status"));}
    @GetMapping("/stats/dashboard") public DashboardDto stats(){return dashboard.getStats();}
    @GetMapping("/impact") public Object impact(){return impactStats.findAll();}
    @GetMapping("/health") public String health(){return "OZIEL OS API is running";}
}
