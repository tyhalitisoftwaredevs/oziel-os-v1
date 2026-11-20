package com.oziel.os.repository;
import com.oziel.os.entity.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface OpportunityRepository extends JpaRepository<Opportunity,Long> {}
