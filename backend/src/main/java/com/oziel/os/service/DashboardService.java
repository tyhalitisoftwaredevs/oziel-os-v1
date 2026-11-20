package com.oziel.os.service;
import com.oziel.os.dto.DashboardDto;
import com.oziel.os.repository.BuildLogRepository;
import com.oziel.os.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Set;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class DashboardService {
    private final ProjectRepository projects;
    private final BuildLogRepository logs;
    @Value("${profile.projects-shipped:5}") private int projectsShipped;
    @Value("${profile.total-certificates:12}") private int totalCertificates;
    public DashboardDto getStats(){
        return new DashboardDto(projects.count(),currentStreak(),logs.count(),totalCertificates,0,projectsShipped);
    }
    private int currentStreak(){
        Set<LocalDate> activeDates=logs.findAllByOrderByCreatedAtDesc().stream()
            .map(log->log.getCreatedAt().atZone(ZoneId.of("Africa/Johannesburg")).toLocalDate())
            .collect(Collectors.toSet());
        LocalDate day=LocalDate.now(ZoneId.of("Africa/Johannesburg"));
        if(!activeDates.contains(day)) day=day.minusDays(1);
        int streak=0;
        while(activeDates.contains(day)){
            streak++;
            day=day.minusDays(1);
        }
        return streak;
    }
}
