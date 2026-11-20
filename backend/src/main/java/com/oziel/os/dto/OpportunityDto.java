package com.oziel.os.dto;
import com.oziel.os.entity.OpportunityStatus;
import com.oziel.os.entity.OpportunityType;
import java.time.LocalDate;
public record OpportunityDto(Long id, String name, String company, OpportunityType type, LocalDate closingDate, OpportunityStatus status, String link) {}
