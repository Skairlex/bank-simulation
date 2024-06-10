package com.ntt.transaction.vo;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClienteVO extends PersonaVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotEmpty(message = "El dato password de cuenta es obligatorio llenar.")
    private String password;

    @Builder.Default
    private boolean estado = true;
}
