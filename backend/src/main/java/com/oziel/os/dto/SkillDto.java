package com.oziel.os.dto;
import com.oziel.os.entity.SkillCategory;
import com.oziel.os.entity.SkillLevel;
public record SkillDto(Long id, String name, SkillCategory category, SkillLevel level, Integer confidence, String proofUrl) {}
