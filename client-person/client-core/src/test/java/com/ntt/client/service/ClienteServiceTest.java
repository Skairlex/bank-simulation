package com.ntt.client.service;

import static org.junit.jupiter.api.Assertions.*;


import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.any;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import com.ntt.client.common.ClientConstants;
import com.ntt.client.entity.Cliente;
import com.ntt.client.repository.IClienteRepository;
import com.ntt.client.request.ClientRequest;
import com.ntt.client.utils.BaseResponseVo;
import com.ntt.client.vo.ClienteVO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ClienteServiceTest {

    @Mock
    private IClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllClientes() {
        List<Cliente> clientes = Arrays.asList(new Cliente());
        when(clienteRepository.findAll()).thenReturn(clientes);

        BaseResponseVo response = clienteService.getAllClientes();
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals(1, ((List) response.getData()).size());
    }

    @Test
    void testGetClienteById() {
        Cliente cliente = new Cliente();
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));

        BaseResponseVo response = clienteService.getClienteById(1L);
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNotNull(response.getData());
    }

    @Test
    void testGetClienteById_NotFound() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());

        BaseResponseVo response = clienteService.getClienteById(1L);
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals(ClientConstants.CLIENT_NOT_FOUND_MESSAGE, response.getMessage());
    }

    @Test
    void testGetClienteByIdentification() {
        Cliente cliente = new Cliente();
        when(clienteRepository.findByIdentificacion("123456")).thenReturn(Optional.of(cliente));

        BaseResponseVo response = clienteService.getClienteByIdentification("123456");
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNotNull(response.getData());
    }

    @Test
    void testGetClienteByIdentification_NotFound() {
        when(clienteRepository.findByIdentificacion("123456")).thenReturn(Optional.empty());

        BaseResponseVo response = clienteService.getClienteByIdentification("123456");
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals(ClientConstants.CLIENT_NOT_FOUND_MESSAGE, response.getMessage());
    }

    @Test
    void testCreateCliente() {
        ClienteVO clienteVO = ClienteVO.builder().identificacion("123456").password("password").build();
        when(clienteRepository.existsByIdentificacion("123456")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(new Cliente());

        BaseResponseVo response = clienteService.createCliente(clienteVO);
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNotNull(response.getData());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testCreateCliente_ExistingIdentification() {
        ClienteVO clienteVO = ClienteVO.builder().identificacion("123456").build();
        when(clienteRepository.existsByIdentificacion("123456")).thenReturn(true);

        BaseResponseVo response = clienteService.createCliente(clienteVO);
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals(ClientConstants.EXIST_IDENTIFICATION_MESSAGE, response.getMessage());
    }

    @Test
    void testUpdateCliente() {
        ClientRequest clientRequest = ClientRequest.builder().identificacion("123456").password("newPassword").build();
        Cliente cliente = new Cliente();
        when(clienteRepository.findByIdentificacion("123456")).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenReturn(cliente);

        BaseResponseVo response = clienteService.updateCliente(clientRequest);
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertNotNull(response.getData());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void testUpdateCliente_NotFound() {
        ClientRequest clientRequest = ClientRequest.builder().identificacion("123456").build();
        when(clienteRepository.findByIdentificacion("123456")).thenReturn(Optional.empty());

        BaseResponseVo response = clienteService.updateCliente(clientRequest);
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals(ClientConstants.CLIENT_NOT_FOUND_MESSAGE, response.getMessage());
    }

    @Test
    void testDeleteCliente() {
        Cliente cliente = new Cliente();
        when(clienteRepository.findByIdentificacion("123456")).thenReturn(Optional.of(cliente));

        BaseResponseVo response = clienteService.deleteCliente("123456");
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals(ClientConstants.SUCCESSFUL_CLIENT_DELETE, response.getMessage());
        verify(clienteRepository, times(1)).deleteByIdentificacion("123456");
    }

    @Test
    void testDeleteCliente_NotFound() {
        when(clienteRepository.findByIdentificacion("123456")).thenReturn(Optional.empty());

        BaseResponseVo response = clienteService.deleteCliente("123456");
        assertNotNull(response);
        assertEquals(200, response.getCode());
        assertEquals(ClientConstants.CLIENT_NOT_FOUND_MESSAGE, response.getMessage());
        verify(clienteRepository, times(0)).deleteByIdentificacion("123456");
    }
}
