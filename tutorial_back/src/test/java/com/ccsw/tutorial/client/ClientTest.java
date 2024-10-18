package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.Client;
import com.ccsw.tutorial.client.model.ClientDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientTest {

    @Mock       // simula comportamiento interfaz ClientRepository
    private ClientRepository clientRepository;

    @InjectMocks    // señala q se inyecte el mock de ClientRepository para utilizar el repositorio ficticio y no el real
    private ClientServiceImpl clientServiceImpl;

    @Test   // comprueba funcionamiento de metodo findAll
    public void findAllShouldReturnAllClients() {
        List<Client> mockClients = new ArrayList<>();   //nueva lista de clientes ficticios
        mockClients.add(mock(Client.class));        // añade cliente simulado a la lista

        when(clientRepository.findAll()).thenReturn(mockClients);   // indica q el repositorio debe devolver la lista

        List<Client> clients = clientServiceImpl.findAll();     // llama al metodo del servicio, q llamara al del repositorio

        assertNotNull(clients);
        assertEquals(1, clients.size());
    }

    public static final String CLIENT_NAME = "Perico Palotes";

    @Test       // comprueba funcionamiento insercion de cliente
    public void saveNotExistsClientIdShouldInsert() throws Exception {
        ClientDto clientDto = new ClientDto();      // crea un nuevo cliente
        clientDto.setName(CLIENT_NAME);

        // crea capturador para verificar qué objeto Client se pasa al repositorio
        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);

        clientServiceImpl.save(null, clientDto);    // deberia crear nuevo cliente

        // verifica q se ha llamado al objeto save del repositorio,
        // captura el objeto con el q ha sido llamado y lo almacena en el clientCaptor
        verify(clientRepository).save(clientCaptor.capture());

        assertEquals(CLIENT_NAME, clientCaptor.getValue().getName());   // comprueba q el objeto creado y el q ha llamado al save son el mismo
    }

    public static final Long EXISTING_CLIENT_ID = 1L;

    @Test       // comprueba que un cliente con id existente se actualiza
    public void saveExistsClientIdShouldUpdate() throws Exception {
        ClientDto clientDto = new ClientDto();      // crea nuevo objeto cliente
        clientDto.setName(CLIENT_NAME);

        Client existingClient = mock(Client.class); // cliente simulado q representa uno existente
        // cuando se busca por id del cliente debe devolver el cliente simulado
        when(clientRepository.findById(EXISTING_CLIENT_ID)).thenReturn(Optional.of(existingClient));

        clientServiceImpl.save(EXISTING_CLIENT_ID, clientDto);      // llama al metodo con id existente

        verify(clientRepository).save(existingClient);      // verifica q ha guardado el cliente actualizado
    }

    @Test       // comprueba que se elimina cliente con id existente
    public void deleteExistsClientIdShouldDelete() throws Exception {
        Client existingClient = mock(Client.class);

        //indica q si en el repositorio se busca por id existente, te devuelve el cliente
        when(clientRepository.findById(EXISTING_CLIENT_ID)).thenReturn(Optional.of(existingClient));

        clientServiceImpl.delete(EXISTING_CLIENT_ID);   // comprueba servicio de borrar por id

        verify(clientRepository).deleteById(EXISTING_CLIENT_ID);    // verifica q se haya llamado al metodo del repositorio
    }

    @Test       // verifica q devuelva cliente si le pasamos id existente
    public void getExistsClientIdShouldReturnClient() {
        Client existingClient = mock(Client.class);

        when(existingClient.getId()).thenReturn(EXISTING_CLIENT_ID);   // el get de un cliente devuelve el id
        //indica q si en el repositorio se busca por id existente, te devuelve el cliente
        when(clientRepository.findById(EXISTING_CLIENT_ID)).thenReturn(Optional.of(existingClient));

        Client clientResponse = clientServiceImpl.get(EXISTING_CLIENT_ID);  // prueba el servicio del get

        assertNotNull(clientResponse);
        assertEquals(EXISTING_CLIENT_ID, clientResponse.getId());   // el id buscado y el del cliente devuelto es el mismo
    }

    public static final Long NOT_EXISTS_CLIENT_ID = 0L;

    @Test       // comprueba q devuelve null al buscar cliente con id inexistente
    public void getNotExistsClientIdShouldReturnNull() {
        when(clientRepository.findById(NOT_EXISTS_CLIENT_ID)).thenReturn(Optional.empty()); // indica al repositorio q debe devolver null

        Client clientResponse = clientServiceImpl.get(NOT_EXISTS_CLIENT_ID);    // llama al metodo get

        assertNull(clientResponse); // devuelve null
    }

    public static final String EXISTS_CLIENT_NAME = "Juana García";

    @Test   // comprueba q devuelva error al guardar un cliente con nombre existente
    public void savesExistClientNameShouldReturnError() {
        ClientDto clientDto = new ClientDto();
        clientDto.setName(EXISTS_CLIENT_NAME);

        when(clientRepository.existsByName(EXISTS_CLIENT_NAME)).thenReturn(true);   // le dice al repositorio q devuelva true si existe

        assertThrows(Exception.class, () -> clientServiceImpl.save(null, clientDto));   // verifica q se lanza excepcion

        verify(clientRepository, never()).save(any(Client.class));  // comprueba q el repositorio no haya llamado nunca al metodo save
    }
}

