package com.feedback.poc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Rating {

    private String employee_id;
    private String manager_id;
    private String employee_rating;
    private String manager_rating;
}
