package com.example.microservicecliente.repository;

import com.example.microservicecliente.domain.Cliente;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ClienteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ClienteRepository clienteRepository;

    @Test
    public void testSaveAndFindClienteByClienteId() {
        // Arrange: Crear un cliente
        Cliente cliente = new Cliente();
        cliente.setNombre("Jose Lema");
        cliente.setGenero("M");
        cliente.setEdad(30);
        cliente.setIdentificacion("123456789");
        cliente.setDireccion("Otavalo sn y principal");
        cliente.setTelefono("098254785");
        cliente.setClienteId("C001");
        cliente.setContrasena("1234");
        cliente.setEstado(true);

        // Act: Guardar el cliente en la base de datos en memoria
        entityManager.persist(cliente);
        entityManager.flush();

        // Buscar el cliente por clienteId
        Cliente foundCliente = clienteRepository.findByClienteId("C001");

        // Assert: Verificar que el cliente se guardó y se encontró correctamente
        assertNotNull(foundCliente, "El cliente debería existir");
        assertEquals("Jose Lema", foundCliente.getNombre(), "El nombre debería coincidir");
        assertEquals("C001", foundCliente.getClienteId(), "El clienteId debería coincidir");
        assertTrue(foundCliente.isEstado(), "El estado debería ser true");
    }
}