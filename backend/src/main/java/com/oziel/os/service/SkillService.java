package com.oziel.os.service;
import com.oziel.os.dto.SkillDto;
import com.oziel.os.entity.SkillCategory;
import com.oziel.os.repository.SkillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.*;

@Service @RequiredArgsConstructor
public class SkillService {
    private final SkillRepository repository;
    public Map<SkillCategory,List<SkillDto>> findGrouped(){
        Map<SkillCategory,List<SkillDto>> result=new EnumMap<>(SkillCategory.class);
        repository.findAll().forEach(s -> result.computeIfAbsent(s.getCategory(),k -> new ArrayList<>())
            .add(new SkillDto(s.getId(),s.getName(),s.getCategory(),s.getLevel(),s.getConfidence(),s.getProofUrl())));
        return result;
    }
}
