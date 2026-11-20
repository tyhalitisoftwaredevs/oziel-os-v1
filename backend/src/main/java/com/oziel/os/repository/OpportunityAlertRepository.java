package com.oziel.os.repository;
import com.oziel.os.entity.DeliveryStatus;
import com.oziel.os.entity.OpportunityAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.Instant;
import java.util.List;
public interface OpportunityAlertRepository extends JpaRepository<OpportunityAlert,Long> {
    List<OpportunityAlert> findTop50ByStatusAndNextAttemptAtLessThanEqualOrderByCreatedAtAsc(DeliveryStatus status, Instant now);
    List<OpportunityAlert> findTop50ByOrderByCreatedAtDesc();
}
