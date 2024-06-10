package com.ntt.client.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientRequest {

    private Long id;


    private String nombre;

    private String genero;

    private int edad;

    @NotNull(message = "El campo identificacion es obligatorio llenar.")
    private String identificacion;

    private String direccion;

    private String telefono;

    private String password;

    @Builder.Default
    private boolean estado = true;
}
