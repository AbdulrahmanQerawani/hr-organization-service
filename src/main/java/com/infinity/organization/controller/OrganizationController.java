package com.infinity.organization.controller;

import com.infinity.organization.client.GatewayClientService;
import com.infinity.organization.model.Department;
import com.infinity.organization.model.Employee;
import com.infinity.organization.model.Organization;
import com.infinity.organization.service.OrganizationService;
import jakarta.annotation.security.RolesAllowed;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/organization")
@RequiredArgsConstructor
public class OrganizationController {
    private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);
    private final OrganizationService organizationService;
    //    private final DepartmentFeignClient departmentFeignClient;
//    private final EmployeeFeignClient employeeFeignClient;
    private final GatewayClientService clientService;

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping
    public List<Organization> findAll() {
        LOGGER.info("Organization find");
        return organizationService.finAll();
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/{organizationId}")
    public Organization findById(@PathVariable("organizationId") Long id) {
        LOGGER.info("Organization find: id={}", id);
        return organizationService.finById(id);
    }

    @RolesAllowed({"ADMIN", "USER"})
    @GetMapping("/{organizationId}/departments")
    public Organization findAllDepartmentByOrganizationId(@PathVariable("organizationId") Long organizationId) {
        LOGGER.info("find departments related to organization request -> /{}/departments", organizationId);
        Organization organization = organizationService.finById(organizationId);
        // get departments list from client Service
        List<Department> departmentsList = clientService.findAllDepartments(organizationId);
        // add departments list to organization
        organization.setDepartments(departmentsList);
        return organization;
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/{organizationId}/departments/with-employees")
    public ResponseEntity<Organization> findAllDepartmentsByOrganizationIdWithEmployees(@PathVariable("organizationId") Long organizationId) {
        LOGGER.info("find departments with all employees related to organization request ->: /{}/departments/with-employees", organizationId);
        // get organization info
        Organization organization = organizationService.finById(organizationId);
        if (organization != null) {
            try {
                // get list of departments in organization
                List<Department> departmentsList = clientService.findAllDepartments(organizationId);

                // get list of employees for each department and add each list to its department
                departmentsList.forEach(department -> {
                    List<Employee> employeeList = clientService.findAllEmployees(organizationId)
                            .stream()
                            .filter(employee -> employee.getDepartmentId().equals(department)).toList();
                    department.setEmployees(employeeList);
                    organization.setEmployees(employeeList);
                });

                // add departmentsList to organization
                organization.setDepartments(departmentsList);
                ResponseEntity.ok(organization);
            } catch (RuntimeException runtimeException) {
                return ResponseEntity.internalServerError().build();
            }
        }

        return ResponseEntity.notFound().build();
    }

    @RolesAllowed("ADMIN")
    @GetMapping("/{organizationId}/employees")
    public ResponseEntity<Organization> findAllEmployeesByOrganizationId(@PathVariable("organizationId") Long organizationId) {
        LOGGER.info("find All employees related to organization -> param: organizationId={}", organizationId);
        // get organization info
        Organization organization = organizationService.finById(organizationId);
        if (organization != null) {
            try {
                // get list of employees and add it to organization object
                organization.setEmployees(clientService.findAllEmployees(organizationId));
                return ResponseEntity.ok(organization);

            } catch (RuntimeException runtimeException) {
                return ResponseEntity.internalServerError().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @RolesAllowed("ADMIN")
    @PostMapping("add")
    public Organization add(@RequestBody Organization organization) {
        LOGGER.info("Organization add: {}", organization);
        return organizationService.add(organization);
    }

}
