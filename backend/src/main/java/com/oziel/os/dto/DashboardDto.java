package com.oziel.os.dto;
public record DashboardDto(long totalProjects, int streakDays, long totalLogs, long totalCertificates,
                           int githubCommitsThisMonth, int projectsShipped) {}
