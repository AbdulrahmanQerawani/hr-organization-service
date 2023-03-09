package com.infinity.organization.service;

import com.infinity.organization.model.Organization;

import java.util.List;

public interface OrganizationService {
List<Organization> finAll();
Organization finById(Long id);
Organization add(Organization organization);

}
