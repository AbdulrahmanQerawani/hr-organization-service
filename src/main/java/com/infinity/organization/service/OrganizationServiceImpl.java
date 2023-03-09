package com.infinity.organization.service;

import com.infinity.organization.model.Organization;
import com.infinity.organization.repository.OrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrganizationServiceImpl implements OrganizationService{
    private final Logger LOGGER = log;
    private final OrganizationRepository organizationRepository;

    public OrganizationServiceImpl(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

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
        Optional<Organization> organization = organizationRepository.findById(id);
        if (organization.isPresent()){
            return organization.get();
        }
        else {
            LOGGER.info("organization with id: {}, not found",id);
        }
        return null;
    }

    @Override
    public Organization add(Organization organization) {
        LOGGER.info("insert new organization with entity-> {}",organization);
        assert (organization.equals(null)):"cannot insert empty Object";
        return organizationRepository.save(organization);

    }
}
