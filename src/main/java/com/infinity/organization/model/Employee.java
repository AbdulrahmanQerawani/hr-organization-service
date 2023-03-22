package com.infinity.organization.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.infinity.organization.common.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Employee {

    private Long employeeId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private int age;
    private String emailAddress;
    private Long organizationId;
    private String organizationName;
    private Long departmentId;
    private String departmentName;
    private String position;
    private Integer version;

}