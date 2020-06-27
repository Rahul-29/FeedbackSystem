package com.feedback.poc.loginservice.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Employee {
    private String employeeId;
    private String firstName;
    private String lastName;
    private CharSequence password;
    private String designation;
    private String managerId;
}
