package com.ntt.client.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ClienteTest {

    @Test
    public void testClienteGettersAndSetters() {
        Cliente cliente = new Cliente();

        cliente.setId(1L);
        cliente.setNombre("John Ponce");
        cliente.setPassword("123");
        cliente.setEstado(true);

        // Verificar que los valores se puedan obtener correctamente
        assertEquals(1L, cliente.getId());
        assertEquals("John Ponce", cliente.getNombre());
        assertEquals("123", cliente.getPassword());
        assertEquals(true, cliente.isEstado());
    }

    @Test
    public void testClienteBuilder() {
        Cliente cliente = Cliente.builder()
            .id(1L)
            .nombre("John Ponce")
            .genero("M")
            .edad(30)
            .identificacion("1234567890")
            .direccion("Calle Principal")
            .telefono("123456789")
            .password("12345")
            .estado(true)
            .build();

        assertEquals(1L, cliente.getId());
        assertEquals("John Ponce", cliente.getNombre());
        assertEquals("M", cliente.getGenero());
        assertEquals(30, cliente.getEdad());
        assertEquals("1234567890", cliente.getIdentificacion());
        assertEquals("Calle Principal", cliente.getDireccion());
        assertEquals("123456789", cliente.getTelefono());
        assertEquals("12345", cliente.getPassword());
        assertEquals(true, cliente.isEstado());
    }

    @Test
    public void testClienteConstructor() {
        Cliente cliente = new Cliente("12345",true);
        assertEquals("12345", cliente.getPassword());
        assertEquals(true, cliente.isEstado());
    }



}
