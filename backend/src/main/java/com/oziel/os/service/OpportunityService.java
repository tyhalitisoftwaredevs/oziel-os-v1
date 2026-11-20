package com.oziel.os.service;
import com.oziel.os.dto.OpportunityDto;
import com.oziel.os.entity.*;
import com.oziel.os.exception.ResourceNotFoundException;
import com.oziel.os.repository.OpportunityRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @RequiredArgsConstructor
public class OpportunityService {
    private final OpportunityRepository repository;
    private final OpportunityAlertService alerts;
    public List<OpportunityDto> findAll(){return repository.findAll().stream().map(this::toDto).toList();}
    @Transactional
    public OpportunityDto create(OpportunityDto dto){
        Opportunity saved=repository.save(Opportunity.builder().name(dto.name()).company(dto.company()).type(dto.type())
            .closingDate(dto.closingDate()).status(dto.status()).link(dto.link()).build());
        alerts.enqueue(saved);
        return toDto(saved);
    }
    public OpportunityDto updateStatus(Long id, OpportunityStatus status){
        Opportunity o=repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Opportunity not found"));
        o.setStatus(status); return toDto(repository.save(o));
    }
    private OpportunityDto toDto(Opportunity o){return new OpportunityDto(o.getId(),o.getName(),o.getCompany(),o.getType(),o.getClosingDate(),o.getStatus(),o.getLink());}
}
