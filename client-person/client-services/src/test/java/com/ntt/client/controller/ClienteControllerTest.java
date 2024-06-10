package com.ntt.client.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.ntt.client.request.ClientRequest;
import com.ntt.client.service.IClienteService;
import com.ntt.client.utils.BaseResponseVo;
import com.ntt.client.vo.ClienteVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.util.Arrays;
import java.util.List;


@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private IClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;



    @Test
    public void testGetAllClientes() {
        when(clienteService.getAllClientes()).thenReturn(BaseResponseVo.builder().data(Arrays.asList(new ClienteVO())).build());
        ResponseEntity<BaseResponseVo> response = clienteController.getAllClientes();
        assertEquals(200, response.getBody().getCode());
    }

    @Test
    void testGetClienteById() {
        when(clienteService.getClienteById(1L)).thenReturn(BaseResponseVo.<ClienteVO>builder().data(new ClienteVO()).build());
        ResponseEntity<BaseResponseVo> response = clienteController.getClienteById(1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetClienteByIdentification() {
        when(clienteService.getClienteByIdentification("123456")).thenReturn(BaseResponseVo.<ClienteVO>builder().data(new ClienteVO()).build());
        ResponseEntity<BaseResponseVo> response = clienteController.getClienteByIdentification("123456");
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testCreateCliente() {
        ClienteVO clienteVO = ClienteVO.builder().nombre("John").genero("Male").edad(30).identificacion("123456").direccion("Somewhere").telefono("1234567890").password("password").build();
        when(clienteService.createCliente(any(ClienteVO.class))).thenReturn(BaseResponseVo.<ClienteVO>builder().data(clienteVO).build());
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(bindingResult.hasErrors()).thenReturn(false);
        ResponseEntity<BaseResponseVo> response = clienteController.createCliente(clienteVO, bindingResult);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testUpdateCliente() {
        ClientRequest clientRequest = ClientRequest.builder().id(1L).nombre("John").genero("Male").edad(30).identificacion("123456").direccion("Somewhere").telefono("1234567890").password("password").build();
        when(clienteService.updateCliente(any(ClientRequest.class))).thenReturn(BaseResponseVo.<ClientRequest>builder().data(clientRequest).build());

        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        ResponseEntity<BaseResponseVo> response = clienteController.updateCliente(clientRequest, bindingResult);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testDeleteCliente() {
        when(clienteService.deleteCliente("123456")).thenReturn(BaseResponseVo.builder().message("Cliente eliminado").build());
        ResponseEntity<BaseResponseVo> response = clienteController.deleteCliente("123456");
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testCreateClienteWithValidationErrors() {
        ClienteVO clienteVO = ClienteVO.builder().nombre("").genero("Male").edad(-1).identificacion("123456").direccion("Somewhere").telefono("1234567890").password("password").build();
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(new ObjectError("clienteVO", "Validation error")));

        ResponseEntity<BaseResponseVo> response = clienteController.createCliente(clienteVO, bindingResult);
        assertEquals(400, response.getStatusCodeValue());
    }

}

