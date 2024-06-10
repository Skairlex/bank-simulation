package com.ntt.transaction.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import com.ntt.transaction.entity.Cuenta;
import com.ntt.transaction.entity.Movimiento;
import com.ntt.transaction.repository.ICuentaRepository;
import com.ntt.transaction.repository.IMovimientosRepository;
import com.ntt.transaction.utils.BaseResponseVo;
import com.ntt.transaction.vo.CuentaVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @Mock
    private ICuentaRepository cuentaRepository;

    @Mock
    private IMovimientosRepository movimientoRepository;

    @InjectMocks
    private ReportService reportService;

    private Cuenta cuenta;
    private Movimiento movimiento1, movimiento2;
    private List<Movimiento> movimientos;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setNumeroCuenta("123456789");
        cuenta.setTipoCuenta("Ahorros");
        cuenta.setSaldoInicial(BigDecimal.valueOf(1000.00));
        cuenta.setEstado("Activo");

        movimiento1 = new Movimiento();
        movimiento1.setId(1L);
        movimiento1.setFecha(LocalDateTime.of(2024, 1, 1, 10, 0));
        movimiento1.setTipoMovimiento("Depósito");
        movimiento1.setValor(BigDecimal.valueOf(500.00));
        movimiento1.setSaldo(BigDecimal.valueOf(1500.00));
        movimiento1.setCuenta(cuenta);

        movimiento2 = new Movimiento();
        movimiento2.setId(2L);
        movimiento2.setFecha(LocalDateTime.of(2024, 1, 2, 10, 0));
        movimiento2.setTipoMovimiento("Retiro");
        movimiento2.setValor(BigDecimal.valueOf(200.00));
        movimiento2.setSaldo(BigDecimal.valueOf(1300.00));
        movimiento2.setCuenta(cuenta);

        movimientos = Arrays.asList(movimiento1, movimiento2);
    }

    @Test
    public void testGenerateAccountStatementReport_CuentaEncontrada() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findByCuentaAndFechaBetween(any(Cuenta.class), any(), any()))
            .thenReturn(movimientos);

        BaseResponseVo response = reportService.generateAccountStatementReport(1L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));

        assertEquals(cuenta.getNumeroCuenta(), ((CuentaVO) response.getData()).getNumeroCuenta());
        assertEquals(2, ((CuentaVO) response.getData()).getMovimientos().size());
    }

    @Test
    public void testGenerateAccountStatementReport_CuentaNoEncontrada() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.empty());

        BaseResponseVo response = reportService.generateAccountStatementReport(1L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));

        assertEquals("Cuenta no encontrada.", response.getMessage());
    }

    @Test
    public void testGenerateAccountStatementPdf_CuentaEncontrada() throws IOException {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findByCuentaAndFechaBetween(any(Cuenta.class), any(), any()))
            .thenReturn(movimientos);

        ByteArrayInputStream pdfStream = reportService.generateAccountStatementPdf(1L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));

        byte[] pdfBytes = pdfStream.readAllBytes();

        assertTrue(pdfBytes.length > 0, "El PDF no debería estar vacío");

    }

    @Test
    public void testGenerateAccountStatementPdf_CuentaNoEncontrada() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            reportService.generateAccountStatementPdf(1L, LocalDate.of(2024, 1, 1), LocalDate.of(2024, 1, 31));
        });
    }
}
