package com.kathemica.demo.hexarchitecture.app.infraestructure.adapters.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentCreateRequest {

    //NotEmpty pasa un string con caracter blanco, para este caso se usa notBlank para que no pase
    @NotBlank(message = "Field firstname cannot be empty or null.")
    private String firstname;

    @NotBlank(message = "Field lastname cannot be empty or null.")
    private String lastname;

    @NotNull(message = "Field age cannot be null.")
    private Integer age;

    @NotBlank(message = "Field address cannot be empty or null.")
    private String address;

}
