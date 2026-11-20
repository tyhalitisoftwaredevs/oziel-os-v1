package com.oziel.os.service;

import com.oziel.os.dto.OpportunityDto;
import com.oziel.os.entity.Opportunity;
import com.oziel.os.entity.OpportunityStatus;
import com.oziel.os.entity.OpportunityType;
import com.oziel.os.repository.OpportunityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OpportunityServiceTest {
    @Mock OpportunityRepository repository;
    @Mock OpportunityAlertService alerts;
    @InjectMocks OpportunityService service;

    @Test void creatingOpportunityQueuesAlertsForDelivery() {
        when(repository.save(any(Opportunity.class))).thenAnswer(invocation -> {
            Opportunity opportunity=invocation.getArgument(0);
            opportunity.setId(8L);
            return opportunity;
        });
        var dto=new OpportunityDto(null,"Software Intern","Example Tech",OpportunityType.INTERNSHIP,
            LocalDate.now().plusMonths(1),OpportunityStatus.WISHLIST,"https://example.com/careers");

        var created=service.create(dto);

        assertEquals(8L,created.id());
        verify(alerts).enqueue(any(Opportunity.class));
    }
}
