package com.infinity.organization.client;

import com.infinity.organization.model.Department;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "department-service")
public interface DepartmentFeignClient {
    @GetMapping("/api/v2/organization/{organizationId}")
    List<Department> findByOrganization(@PathVariable("organizationId") Long organizationId);

    @GetMapping("/api/v2/organization/{organizationId}/with-employees")
    List<Department> findByOrganizationWithEmployees(@PathVariable("organizationId") Long organizationId);

}