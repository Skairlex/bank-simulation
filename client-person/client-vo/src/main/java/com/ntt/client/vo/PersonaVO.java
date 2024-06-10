package com.ntt.client.vo;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonaVO {

    private Long id;

    @NotEmpty(message = "El nombre es obligatorio llenar.")
    private String nombre;

    @NotEmpty(message = "El genero es obligatorio llenar.")
    private String genero;

    @NotNull(message = "La edad obligatorio llenar.")
    @Positive(message = "La edad debe ser un número positivo.")
    private int edad;

    @NotNull(message = "El campo identificacion es obligatorio llenar.")
    private String identificacion;

    @NotNull(message = "El camop direccion es obligatorio llenar.")
    private String direccion;

    @NotNull(message = "El campo telefono es obligatorio llenar.")
    private String telefono;
}
