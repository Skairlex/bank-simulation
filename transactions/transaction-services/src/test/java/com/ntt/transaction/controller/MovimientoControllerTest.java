package com.ntt.transaction.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.ntt.transaction.request.MovimientoUpdateRequest;
import com.ntt.transaction.service.IMovimientoService;
import com.ntt.transaction.utils.BaseResponseVo;
import com.ntt.transaction.vo.MovimientoVO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;


import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class MovimientoControllerTest {

    @Mock
    private IMovimientoService movimientosService;

    @InjectMocks
    private MovimientoController movimientoController;

    @Test
    public void testGetAllMovimientos() {
        when(movimientosService.getAllMovimientos()).thenReturn(BaseResponseVo.builder().data(Collections.emptyList()).build());

        ResponseEntity<BaseResponseVo> response = movimientoController.getAllMovimientos();

        assertEquals(200, response.getBody().getCode());
        assertTrue( response.getBody().getData()!=null);
    }

    @Test
    public void testGetMovimientoById() {
        Long id = 1L;
        MovimientoVO movimiento = new MovimientoVO();
        when(movimientosService.getMovimientoById(id)).thenReturn(BaseResponseVo.builder().data(movimiento).build());

        ResponseEntity<BaseResponseVo> response = movimientoController.getMovimientoById(id);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(movimiento, response.getBody().getData());
    }

    @Test
    public void testCreateMovimiento_ValidData() {
        MovimientoVO movimiento = new MovimientoVO();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(movimientosService.createMovimiento(movimiento)).thenReturn(BaseResponseVo.builder().data(movimiento).build());

        ResponseEntity<BaseResponseVo> response = movimientoController.createMovimiento(movimiento, bindingResult);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(movimiento, response.getBody().getData());
    }

    @Test
    public void testCreateMovimiento_InvalidData() {
        MovimientoVO movimiento = new MovimientoVO();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(
            Collections.singletonList(new ObjectError("field", "error message"))
        );

        ResponseEntity<BaseResponseVo> response = movimientoController.createMovimiento(movimiento, bindingResult);

        assertEquals(400, response.getStatusCodeValue());
        assertFalse(((List<?>) response.getBody().getErrors()).isEmpty());
    }

    @Test
    public void testUpdateMovimiento_ValidData() {
        MovimientoUpdateRequest movimientosDetails = new MovimientoUpdateRequest();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(movimientosService.updateMovimiento(movimientosDetails)).thenReturn(BaseResponseVo.builder().data(movimientosDetails).build());

        ResponseEntity<BaseResponseVo> response = movimientoController.updateMovimiento(movimientosDetails, bindingResult);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(movimientosDetails, response.getBody().getData());
    }

    @Test
    public void testUpdateMovimiento_InvalidData() {
        MovimientoUpdateRequest movimientosDetails = new MovimientoUpdateRequest();
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(
            Collections.singletonList(new ObjectError("field", "error message"))
        );

        ResponseEntity<BaseResponseVo> response = movimientoController.updateMovimiento(movimientosDetails, bindingResult);

        assertEquals(400, response.getStatusCodeValue());
        assertFalse(((List<?>) response.getBody().getErrors()).isEmpty());
    }

    @Test
    public void testDeleteMovimiento() {
        Long id = 1L;
        when(movimientosService.deleteMovimiento(id)).thenReturn(BaseResponseVo.builder().code(200).build());

        ResponseEntity<BaseResponseVo> response = movimientoController.deleteMovimiento(id);

        assertEquals(200, response.getStatusCodeValue());
    }
}
