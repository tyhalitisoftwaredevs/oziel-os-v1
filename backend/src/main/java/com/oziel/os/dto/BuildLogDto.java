package com.oziel.os.dto;
import com.oziel.os.entity.LogCategory;
import java.time.Instant;
public record BuildLogDto(Long id, String title, String content, LogCategory category, String tags, Instant createdAt, String imageUrl) {}
