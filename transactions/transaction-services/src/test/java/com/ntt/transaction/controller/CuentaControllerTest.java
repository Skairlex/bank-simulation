package com.ntt.transaction.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import com.ntt.transaction.service.ICuentaService;
import com.ntt.transaction.utils.BaseResponseVo;
import com.ntt.transaction.vo.CuentaVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

@ExtendWith(MockitoExtension.class)
class CuentaControllerTest {

    @Mock
    private ICuentaService cuentaService;

    @InjectMocks
    private CuentaController cuentaController;

    @Test
    public void testGetAllCuentas() {
        List<CuentaVO> cuentas = Arrays.asList(new CuentaVO(), new CuentaVO());
        Mockito.when(cuentaService.getAllCuentas()).thenReturn(BaseResponseVo.builder().code(200).data(cuentas).build());
        ResponseEntity<BaseResponseVo> response = cuentaController.getAllCuentas();

        assertEquals(200, response.getBody().getCode());
        assertEquals(cuentas, response.getBody().getData());
    }

    @Test
    public void testGetCuentaById() {
        Long id = 1L;
        CuentaVO cuenta = new CuentaVO();
        Mockito.when(cuentaService.getCuentaById(id)).thenReturn( BaseResponseVo.builder().code(200).data(cuenta).build());

        ResponseEntity<BaseResponseVo> response = cuentaController.getCuentaById(id);

        assertEquals(200, response.getBody().getCode());
        assertEquals(cuenta, response.getBody().getData());
    }

    @Test
    public void testGetByAccountNumber() {
        String number = "123456";
        CuentaVO cuenta = new CuentaVO();
        Mockito.when(cuentaService.getCuentaByAccountNumber(number)).thenReturn(BaseResponseVo.builder().code(200).data(cuenta).build());

        ResponseEntity<BaseResponseVo> response = cuentaController.getByAccountNumber(number);

        assertEquals(200, response.getBody().getCode());
        assertEquals(cuenta, response.getBody().getData());
    }

    @Test
    public void testCreateCuenta_ValidData() {
        CuentaVO cuenta = new CuentaVO();
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.when(cuentaService.createCuenta(cuenta)).thenReturn(BaseResponseVo.builder().code(200).data(cuenta).build());

        ResponseEntity<BaseResponseVo> response = cuentaController.createCuenta(cuenta, bindingResult);

        assertEquals(200, response.getBody().getCode());
        assertEquals(cuenta, response.getBody().getData());
    }

    @Test
    public void testCreateCuenta_InvalidData() {
        CuentaVO cuenta = new CuentaVO();
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        Mockito.when(bindingResult.getAllErrors()).thenReturn(
            Arrays.asList(new ObjectError("field", "error message"))
        );

        ResponseEntity<BaseResponseVo> response = cuentaController.createCuenta(cuenta, bindingResult);

        assertEquals(400, response.getBody().getCode());
        assertFalse(response.getBody().getErrors().isEmpty());
    }

    @Test
    public void testUpdateCuenta_ValidData() {
        CuentaVO cuenta = new CuentaVO();
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        Mockito.when(cuentaService.updateCuenta(cuenta)).thenReturn(BaseResponseVo.builder().code(200).data(cuenta).build());

        ResponseEntity<BaseResponseVo> response = cuentaController.updateCuenta(cuenta, bindingResult);

        assertEquals(200, response.getBody().getCode());
        assertEquals(cuenta, response.getBody().getData());
    }

    @Test
    public void testUpdateCuenta_InvalidData() {
        CuentaVO cuenta = new CuentaVO();
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(true);
        Mockito.when(bindingResult.getAllErrors()).thenReturn(
            Collections.singletonList(new ObjectError("field", "error message"))
        );

        ResponseEntity<BaseResponseVo> response = cuentaController.updateCuenta(cuenta, bindingResult);

        assertEquals(400, response.getBody().getCode());
        assertFalse(response.getBody().getErrors().isEmpty());
    }

    @Test
    public void testDeleteCuenta() {
        Long id = 1L;
        Mockito.when(cuentaService.deleteCuenta(id)).thenReturn(BaseResponseVo.builder().code(200).build());

        ResponseEntity<BaseResponseVo> response = cuentaController.deleteCuenta(id);

        assertEquals(200, response.getBody().getCode());
    }

}
