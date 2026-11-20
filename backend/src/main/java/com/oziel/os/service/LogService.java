package com.oziel.os.service;
import com.oziel.os.dto.BuildLogDto;
import com.oziel.os.entity.BuildLog;
import com.oziel.os.entity.LogCategory;
import com.oziel.os.repository.BuildLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service @RequiredArgsConstructor
public class LogService {
    private final BuildLogRepository repository;
    public List<BuildLogDto> findAll(LogCategory category) {
        var logs=category==null ? repository.findAllByOrderByCreatedAtDesc() : repository.findByCategoryOrderByCreatedAtDesc(category);
        return logs.stream().map(l -> new BuildLogDto(l.getId(),l.getTitle(),l.getContent(),l.getCategory(),l.getTags(),l.getCreatedAt(),l.getImageUrl())).toList();
    }
    public BuildLogDto create(BuildLogDto dto) {
        BuildLog log=BuildLog.builder().title(dto.title()).content(dto.content()).category(dto.category()).tags(dto.tags()).imageUrl(dto.imageUrl()).build();
        BuildLog saved=repository.save(log);
        return new BuildLogDto(saved.getId(),saved.getTitle(),saved.getContent(),saved.getCategory(),saved.getTags(),saved.getCreatedAt(),saved.getImageUrl());
    }
}
