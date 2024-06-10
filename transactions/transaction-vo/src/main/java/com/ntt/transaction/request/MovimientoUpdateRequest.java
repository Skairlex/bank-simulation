package com.ntt.transaction.request;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.ntt.transaction.util.NonZero;
import jakarta.validation.constraints.NotEmpty;
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
public class MovimientoUpdateRequest {

    @NotNull(message = "El dato tipo de movimiento es obligatorio llenar.")
    private Long id;

    @Builder.Default
    private LocalDateTime fecha=LocalDateTime.now();

    @NotEmpty(message = "El dato tipo de movimiento es obligatorio llenar.")
    private String tipoMovimiento;

    @NotNull(message = "El dato valor es obligatorio llenar.")
    @NonZero(message = "El valor no puede ser cero.")
    private BigDecimal valor;

}
