package com.infinity.organization.service;

import com.infinity.organization.model.Organization;
import com.infinity.organization.repository.OrganizationRepository;
import io.micrometer.observation.annotation.Observed;
import io.micrometer.tracing.ScopedSpan;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Observed(name = "get Organization Db Call")
public class OrganizationServiceImpl implements OrganizationService{
    private final Logger LOGGER = log;
    private final OrganizationRepository organizationRepository;
    private final Tracer tracer;
    @Override
    public List<Organization> finAll() {
        LOGGER.info("find all organizations");
        List<Organization> organizationList = organizationRepository.findAll();
        if (organizationList.isEmpty()){
            LOGGER.info("No organizations found");
        }
        return organizationList;
    }

    @Override
    public Organization finById(Long id) {
        LOGGER.info("find organization with id: {}",id);
        ScopedSpan newSpan = tracer.startScopedSpan("get Organization DB Call");
        try {
            Optional<Organization> organization = organizationRepository.findById(id);
            if (organization.isPresent()) {
                return organization.get();
            } else {
                String message = String.format("Unable to find an organization with theOrganization id %s", id);
                LOGGER.error(message);
                throw new IllegalArgumentException(message);
            }
        }
        finally {
            newSpan.tag("peer.service", "postgres");
            newSpan.event("Client received");
            newSpan.end();
        }
    }

    @Override
    public Organization add(Organization organization) {
        LOGGER.info("insert new organization with entity-> {}",organization);
        assert (organization.equals(null)):"cannot insert empty Object";
        return organizationRepository.save(organization);

    }
}
