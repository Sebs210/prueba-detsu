package com.example.microservicecliente.integration;

import com.example.microservicecliente.domain.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClienteControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testCreateCliente() {
        // Arrange: Crear un cliente para enviar al endpoint
        Cliente cliente = new Cliente();
        cliente.setNombre("Marianela Montalvo");
        cliente.setGenero("F");
        cliente.setEdad(25);
        cliente.setIdentificacion("987654321");
        cliente.setDireccion("Amazonas y NNUU");
        cliente.setTelefono("097548965");
        cliente.setClienteId("C002");
        cliente.setContrasena("5678");
        cliente.setEstado(true);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Cliente> request = new HttpEntity<>(cliente, headers);

        // Act: Hacer una solicitud POST al endpoint /clientes
        String url = "http://localhost:" + port + "/clientes";
        ResponseEntity<Cliente> response = restTemplate.exchange(url, HttpMethod.POST, request, Cliente.class);

        // Assert: Verificar la respuesta
        assertEquals(200, response.getStatusCode().value(), "El estado debería ser 200 OK");
        Cliente createdCliente = response.getBody();
        assertNotNull(createdCliente, "El cliente creado no debería ser nulo");
        assertNotNull(createdCliente.getId(), "El ID debería ser generado");
        assertEquals("Marianela Montalvo", createdCliente.getNombre(), "El nombre debería coincidir");
        assertEquals("C002", createdCliente.getClienteId(), "El clienteId debería coincidir");

        // Verificar que se puede recuperar el cliente creado
        ResponseEntity<Cliente> getResponse = restTemplate.getForEntity(url + "/" + createdCliente.getId(), Cliente.class);
        assertEquals(200, getResponse.getStatusCode().value(), "El estado debería ser 200 OK");
        Cliente retrievedCliente = getResponse.getBody();
        assertEquals("Marianela Montalvo", retrievedCliente.getNombre(), "El nombre recuperado debería coincidir");
    }
}