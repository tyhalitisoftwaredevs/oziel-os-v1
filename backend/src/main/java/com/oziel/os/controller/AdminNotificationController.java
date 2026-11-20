package com.oziel.os.controller;

import com.oziel.os.dto.OpportunityAlertDto;
import com.oziel.os.service.OpportunityAlertService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/api/v1/admin/notifications") @RequiredArgsConstructor
public class AdminNotificationController {
    private final OpportunityAlertService service;
    @GetMapping public List<OpportunityAlertDto> recent(){return service.recent();}
}
