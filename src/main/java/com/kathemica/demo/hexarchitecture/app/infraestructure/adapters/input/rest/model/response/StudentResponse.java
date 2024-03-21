package com.kathemica.demo.hexarchitecture.app.infraestructure.adapters.input.rest.model.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    private Long id;
    private String firstname;
    private String lastname;
    private Integer age;
    private String address;
}
