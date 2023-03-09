package com.infinity.organization.repository;

import com.infinity.organization.model.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization,Long>{
    Optional<Organization> findById(Long id);
    List<Organization> findAll();

}
