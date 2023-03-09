package com.infinity.organization.client;

import com.infinity.organization.model.Department;
import com.infinity.organization.model.Employee;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class GatewayClientService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayClientService.class);
    private static final String AUTHORIZATION = "Authorization";
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${infinity.gateway.baseUrl}")
    private String baseUrl = "http://localhost:8072";

    /**
     * find All Departments with specified ID
     *
     * @param organizationId specified ID
     * @return List of departments
     * @apiNote this method sends down stream call to department-service throw gateway server
     */
    public List<Department> findAllDepartments(@NotNull Long organizationId) {
        List<Department> departmentList = doRestExchange(baseUrl + "/department/api/v2/organization/{organizationId}", organizationId);
        return departmentList;
    }

    /**
     * find All departments and employees with specified ID
     *
     * @param organizationId specified ID
     * @return List of departments contains list of its employees
     * @apiNote this method sends down stream call to department-service and employee-service throw gateway server
     */
    public List<Department> findAllDepartmentsWithEmployees(@NotNull Long organizationId) {
        List<Department> employeeList = doRestExchange(baseUrl + "/department/api/v2/organization/{organizationId}/with-employees", organizationId);
        return employeeList;
    }

    /**
     * find All employees with specified ID
     *
     * @param organizationId specified ID
     * @return List of employees
     * @apiNote this method sends down stream call to employee-service throw gateway server
     */
    public List<Employee> findAllEmployees(@NotNull Long organizationId) {
        List<Employee> employeeList = doRestExchange(baseUrl + "/employee/api/v2/organization/{organizationId}", organizationId);
        return employeeList;
    }

    private <T> List<T> doRestExchange(@NotNull String clientUrl, Long params) {
        /*** get Authentication token */
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, "Bearer " + jwt.getTokenValue());
        LOGGER.info("down stream call url:{}", clientUrl);
        ParameterizedTypeReference<List<T>> responseType = new ParameterizedTypeReference<>() {};
        List<T> responseBody = new ArrayList<>();
        try {
            return restTemplate.exchange(clientUrl, HttpMethod.GET, new HttpEntity<>(headers), responseType, params)
                    .getBody();
        } catch (ResourceAccessException e) {
            LOGGER.error("Failed to access resource: " + e.getMessage(), e);
            ResponseEntity.internalServerError().build();
        }
        return responseBody;
    }
}
