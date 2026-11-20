package com.oziel.os.repository;
import com.oziel.os.entity.BuildLog;
import com.oziel.os.entity.LogCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface BuildLogRepository extends JpaRepository<BuildLog,Long> {
    List<BuildLog> findAllByOrderByCreatedAtDesc();
    List<BuildLog> findByCategoryOrderByCreatedAtDesc(LogCategory category);
}
