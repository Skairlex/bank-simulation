package com.ntt.transaction.service;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import com.ntt.transaction.common.TransactionsConstants;
import com.ntt.transaction.entity.Cuenta;
import com.ntt.transaction.repository.ICuentaRepository;
import com.ntt.transaction.utils.BaseResponseVo;
import com.ntt.transaction.vo.CuentaVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class CuentaServiceTest {

    @Mock
    private ICuentaRepository cuentaRepository;

    @InjectMocks
    private CuentaService cuentaService;

    @Test
    public void testGetAllCuentas() {
        Cuenta cuenta1 = Cuenta.builder()
            .id(1L)
            .numeroCuenta("12345")
            .tipoCuenta("Ahorro")
            .saldoInicial(BigDecimal.valueOf(1000))
            .estado("Activo")
            .movimientos(Collections.emptyList())
            .build();
        Cuenta cuenta2 = Cuenta.builder()
            .id(2L)
            .numeroCuenta("67890")
            .tipoCuenta("Corriente")
            .saldoInicial(BigDecimal.valueOf(2000))
            .estado("Activo")
            .movimientos(Collections.emptyList())
            .build();

        when(cuentaRepository.findAll()).thenReturn(Arrays.asList(cuenta1, cuenta2));

        BaseResponseVo response = cuentaService.getAllCuentas();
        assertNotNull(response);
        assertEquals(2, ((List<CuentaVO>) response.getData()).size());
    }

    @Test
    public void testGetCuentaById_Exist() {
        Cuenta cuenta = Cuenta.builder()
            .id(1L)
            .numeroCuenta("12345")
            .tipoCuenta("Ahorro")
            .saldoInicial(BigDecimal.valueOf(1000))
            .estado("Activo")
            .movimientos(Collections.emptyList())
            .build();

        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(cuenta));

        BaseResponseVo response = cuentaService.getCuentaById(1L);
        assertNotNull(response);
        assertEquals(cuenta.getNumeroCuenta(), ((CuentaVO) response.getData()).getNumeroCuenta());
    }

    @Test
    public void testGetCuentaById_NotFound() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.empty());

        BaseResponseVo response = cuentaService.getCuentaById(1L);
        assertNotNull(response);
        assertEquals(TransactionsConstants.ACCOUNT_NOT_FOUND_MESSAGE, response.getMessage());
    }

    @Test
    public void testCreateCuenta() {
        CuentaVO cuentaVO = CuentaVO.builder()
            .numeroCuenta("12345")
            .tipoCuenta("Ahorro")
            .saldoInicial(BigDecimal.valueOf(1000))
            .estado("Activo")
            .build();
        Cuenta cuenta = Cuenta.builder()
            .id(1L)
            .numeroCuenta("12345")
            .tipoCuenta("Ahorro")
            .saldoInicial(BigDecimal.valueOf(1000))
            .estado("Activo")
            .movimientos(Collections.emptyList())
            .build();

        when(cuentaRepository.existsByNumeroCuenta("12345")).thenReturn(false);
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

        BaseResponseVo response = cuentaService.createCuenta(cuentaVO);
        assertNotNull(response);
        assertEquals(cuentaVO.getNumeroCuenta(), ((CuentaVO) response.getData()).getNumeroCuenta());
    }

    @Test
    public void testUpdateCuenta() {
        CuentaVO cuentaVO = CuentaVO.builder()
            .id(1L)
            .numeroCuenta("12345")
            .tipoCuenta("Ahorro")
            .saldoInicial(BigDecimal.valueOf(1000))
            .estado("Activo")
            .build();
        Cuenta cuenta = Cuenta.builder()
            .id(1L)
            .numeroCuenta("12345")
            .tipoCuenta("Ahorro")
            .saldoInicial(BigDecimal.valueOf(1000))
            .estado("Activo")
            .movimientos(Collections.emptyList())
            .build();

        when(cuentaRepository.findByNumeroCuenta("12345")).thenReturn(Optional.of(cuenta));
        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuenta);

        BaseResponseVo response = cuentaService.updateCuenta(cuentaVO);
        assertNotNull(response);
        assertEquals(cuentaVO.getNumeroCuenta(), ((CuentaVO) response.getData()).getNumeroCuenta());
    }

    @Test
    public void testDeleteCuenta() {
        when(cuentaRepository.findById(1L)).thenReturn(Optional.of(new Cuenta()));

        BaseResponseVo response = cuentaService.deleteCuenta(1L);
        assertNotNull(response);
        assertEquals(TransactionsConstants.SUCCESSFUL_ACCOUNT_DELETE, response.getMessage());
    }
}
