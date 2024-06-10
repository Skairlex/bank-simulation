package com.ntt.transaction.service;

import static org.junit.jupiter.api.Assertions.*;

import com.ntt.transaction.common.TransactionsConstants;
import com.ntt.transaction.entity.Cuenta;
import com.ntt.transaction.entity.Movimiento;
import com.ntt.transaction.repository.ICuentaRepository;
import com.ntt.transaction.repository.IMovimientosRepository;
import com.ntt.transaction.request.MovimientoUpdateRequest;
import com.ntt.transaction.utils.BaseResponseVo;
import com.ntt.transaction.vo.CuentaVO;
import com.ntt.transaction.vo.MovimientoVO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class MovimientoServiceTest {

    @Mock
    private IMovimientosRepository movimientosRepository;

    @Mock
    private ICuentaRepository cuentaRepository;

    @Mock
    private ICuentaService cuentaService;

    @InjectMocks
    private MovimientoService movimientoService;

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(cuentaRepository.findByNumeroCuenta(anyString()))
            .thenReturn(Optional.of(new Cuenta()));
    }

    @Test
    public void testGetAllMovimientos() {
        Movimiento movimiento1 = Movimiento.builder()
            .id(1L)
            .fecha(LocalDateTime.now())
            .tipoMovimiento("Depósito")
            .valor(BigDecimal.valueOf(500))
            .saldo(BigDecimal.valueOf(1500))
            .cuenta(new Cuenta())
            .build();
        Movimiento movimiento2 = Movimiento.builder()
            .id(2L)
            .fecha(LocalDateTime.now())
            .tipoMovimiento("Retiro")
            .valor(BigDecimal.valueOf(200))
            .saldo(BigDecimal.valueOf(1300))
            .cuenta(new Cuenta())
            .build();

        when(movimientosRepository.findAll()).thenReturn(Arrays.asList(movimiento1, movimiento2));

        BaseResponseVo response = movimientoService.getAllMovimientos();
        assertNotNull(response);
        assertEquals(2, ((List<MovimientoVO>) response.getData()).size());
    }

    @Test
    public void testGetMovimientoById_Exist() {
        Movimiento movimiento = Movimiento.builder()
            .id(1L)
            .fecha(LocalDateTime.now())
            .tipoMovimiento("Depósito")
            .valor(BigDecimal.valueOf(500))
            .saldo(BigDecimal.valueOf(1500))
            .cuenta(new Cuenta())
            .build();

        when(movimientosRepository.findById(1L)).thenReturn(Optional.of(movimiento));

        BaseResponseVo response = movimientoService.getMovimientoById(1L);
        assertNotNull(response);
        assertEquals(movimiento.getId(), ((MovimientoVO) response.getData()).getId());
    }

    @Test
    public void testGetMovimientoById_NotFound() {
        when(movimientosRepository.findById(1L)).thenReturn(Optional.empty());

        BaseResponseVo response = movimientoService.getMovimientoById(1L);
        assertNotNull(response);
        assertEquals(TransactionsConstants.TRANSACTION_NOT_FOUND_MESSAGE, response.getData());
    }

    @Test
    public void testCreateMovimiento_Success() {

        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta("12345");
        cuenta.setTipoCuenta("AHORROS");
        cuenta.setSaldoInicial(BigDecimal.valueOf(1000));

        Movimiento movimiento = new Movimiento();
        movimiento.setTipoMovimiento("DEPOSITO");
        movimiento.setValor(BigDecimal.valueOf(500));
        movimiento.setCuenta(cuenta);

        when(cuentaRepository.findByNumeroCuenta("12345")).thenReturn(Optional.of(cuenta));
        when(movimientosRepository.save(any(Movimiento.class))).thenReturn(movimiento);

        MovimientoVO movimientoVO = new MovimientoVO();
        movimientoVO.setTipoMovimiento("DEPOSITO");
        movimientoVO.setValor(BigDecimal.valueOf(500));
        movimientoVO.setCuenta(CuentaVO.builder().numeroCuenta("12345").tipoCuenta("AHORROS").build());

        BaseResponseVo response = movimientoService.createMovimiento(movimientoVO);

        assertNotNull(response);
        Assertions.assertEquals(movimiento.getId(), ((MovimientoVO) response.getData()).getId());
    }

    @Test
    public void testCreateMovimiento_AccountNotFound() {
        MovimientoVO movimientoVO = MovimientoVO.builder()
            .tipoMovimiento("DEPOSITO")
            .valor(BigDecimal.valueOf(500))
            .cuenta(CuentaVO.builder().numeroCuenta("12345").tipoCuenta("AHORROS").build())
            .build();

        when(cuentaRepository.findByNumeroCuenta(anyString())).thenReturn(Optional.empty());

        BaseResponseVo response = movimientoService.createMovimiento(movimientoVO);
        assertNotNull(response);
        assertEquals(TransactionsConstants.ACCOUNT_NOT_FOUND_MESSAGE, response.getMessage());
    }

    @Test
    public void testUpdateMovimiento_Success() {
        MovimientoUpdateRequest movimientoUpdateRequest = MovimientoUpdateRequest.builder()
            .id(1L)
            .tipoMovimiento("RETIRO")
            .valor(BigDecimal.valueOf(300))
            .fecha(LocalDateTime.now())
            .build();
        Movimiento movimiento = Movimiento.builder()
            .id(1L)
            .fecha(LocalDateTime.now())
            .tipoMovimiento("DEPOSITO")
            .valor(BigDecimal.valueOf(500))
            .saldo(BigDecimal.valueOf(1500))
            .cuenta(Cuenta.builder().numeroCuenta("12345").tipoCuenta("AHORROS").saldoInicial(BigDecimal.valueOf(1500)).build())
            .build();

        when(movimientosRepository.findById(anyLong())).thenReturn(Optional.of(movimiento));
        when(movimientosRepository.save(any(Movimiento.class))).thenReturn(movimiento);

        BaseResponseVo response = movimientoService.updateMovimiento(movimientoUpdateRequest);
        assertNotNull(response);
        assertEquals(movimiento.getId(), ((MovimientoVO) response.getData()).getId());
    }

    @Test
    public void testUpdateMovimiento_NotFound() {
        MovimientoUpdateRequest movimientoUpdateRequest = MovimientoUpdateRequest.builder()
            .id(1L)
            .tipoMovimiento("Retiro")
            .valor(BigDecimal.valueOf(300))
            .fecha(LocalDateTime.now())
            .build();

        when(movimientosRepository.findById(anyLong())).thenReturn(Optional.empty());

        BaseResponseVo response = movimientoService.updateMovimiento(movimientoUpdateRequest);
        assertNotNull(response);
        assertEquals(TransactionsConstants.TRANSACTION_NOT_FOUND_MESSAGE, response.getMessage());
    }

    @Test
    public void testDeleteMovimiento_Success() {
        Movimiento movimiento = Movimiento.builder()
            .id(1L)
            .fecha(LocalDateTime.now())
            .tipoMovimiento("DEPOSITO")
            .valor(BigDecimal.valueOf(500))
            .saldo(BigDecimal.valueOf(1500))
            .cuenta(Cuenta.builder().numeroCuenta("12345").tipoCuenta("AHORROS").saldoInicial(BigDecimal.valueOf(1500)).build())
            .build();

        when(movimientosRepository.findById(anyLong())).thenReturn(Optional.of(movimiento));

        BaseResponseVo response = movimientoService.deleteMovimiento(1L);
        assertNotNull(response);
        assertEquals(TransactionsConstants.SUCCESSFUL_TRANSACTION_DELETE, response.getMessage());
    }

    @Test
    public void testDeleteMovimiento_NotFound() {
        when(movimientosRepository.findById(anyLong())).thenReturn(Optional.empty());

        BaseResponseVo response = movimientoService.deleteMovimiento(1L);
        assertNotNull(response);
        assertEquals(TransactionsConstants.TRANSACTION_NOT_FOUND_MESSAGE, response.getMessage());
    }
}
