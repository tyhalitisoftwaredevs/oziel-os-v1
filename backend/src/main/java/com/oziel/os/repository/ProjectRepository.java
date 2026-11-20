package com.oziel.os.repository;
import com.oziel.os.entity.Project;
import com.oziel.os.entity.ProjectStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface ProjectRepository extends JpaRepository<Project,Long> {
    Optional<Project> findBySlug(String slug);
    List<Project> findByStatus(ProjectStatus status);
    List<Project> findByFeaturedTrue();
}
