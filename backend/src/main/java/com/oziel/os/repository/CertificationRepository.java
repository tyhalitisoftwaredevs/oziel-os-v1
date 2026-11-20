package com.oziel.os.repository;

import com.oziel.os.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {
    List<Certification> findAllByOrderByAwardedOnDesc();
    List<Certification> findByPubliclyVisibleTrueOrderByAwardedOnDesc();
}
