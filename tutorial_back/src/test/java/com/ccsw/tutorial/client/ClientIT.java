package com.ccsw.tutorial.client;

import com.ccsw.tutorial.client.model.ClientDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

// inicializa el contexto de Spring cada vez q se inician los test de JUnit
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// indica que los test son transaccionales y que, al finalizar cada test, Spring deberá dejar la BBDD como estaba
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class ClientIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/client";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<List<ClientDto>> responseType = new ParameterizedTypeReference<List<ClientDto>>() {
    };

    //comprobamos que el findAll devuelve todos los clientes
    @Test
    public void findAllShouldReturnAllClients() {

        ResponseEntity<List<ClientDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);

        assertNotNull(response);
        assertEquals(3, response.getBody().size());
    }

    public static final Long NEW_CLIENT_ID = 4L;
    public static final String NEW_CLIENT_NAME = "Perico Palotes";

    // comprobamos que al hacer save con id nulo, crea un nuevo cliente
    @Test
    public void saveWithoutIdShouldCreateNewClient() {

        ClientDto dto = new ClientDto();
        dto.setName(NEW_CLIENT_NAME);

        //endpoint para añadir el cliente
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        //endpoint para buscar el cliente, si la respuesta no es nula, se ha guardado correctamente
        ResponseEntity<List<ClientDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
        assertNotNull(response);
        assertEquals(4, response.getBody().size());     // deberia haber 3 clientes + 1 añadido

        ClientDto clientSearch = response.getBody().stream().filter(item -> item.getId().equals(NEW_CLIENT_ID)).findFirst().orElse(null); // comprueba que el id es 4
        assertNotNull(clientSearch);
        assertEquals(NEW_CLIENT_NAME, clientSearch.getName());      //comprueba que el nombre se ha guardado bien
    }

    public static final Long MODIFY_CLIENT_ID = 3L;       // id a modificar

    // comprueba que una peticion de modificar un registro con id que existe funciona
    @Test
    public void modifyWithExistIdShouldModifyClient() {

        ClientDto dto = new ClientDto();
        dto.setName(NEW_CLIENT_NAME);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + MODIFY_CLIENT_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        ResponseEntity<List<ClientDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
        assertNotNull(response);
        assertEquals(3, response.getBody().size());     // comprueba que el listado sigue siendo 3 y no se ha añadido un nuevo registro

        ClientDto clientSearch = response.getBody().stream().filter(item -> item.getId().equals(MODIFY_CLIENT_ID)).findFirst().orElse(null);
        assertNotNull(clientSearch);
        assertEquals(NEW_CLIENT_NAME, clientSearch.getName());
    }

    //comprueba que dé un internal error si se intenta modificar un registro con id inexistente
    @Test
    public void modifyWithNotExistIdShouldInternalError() {

        ClientDto dto = new ClientDto();
        dto.setName(NEW_CLIENT_NAME);

        //busca el id 4, que no existe
        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + NEW_CLIENT_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());       // comprueba que el error sea un internal error
    }

    // comprueba que se llame al metodo delete y que despues hay un registro menos
    public static final Long DELETE_CLIENT_ID = 2L;

    @Test
    public void deleteWithExistsIdShouldDeleteClient() {

        // simula la accion de delete
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_CLIENT_ID, HttpMethod.DELETE, null, Void.class);

        ResponseEntity<List<ClientDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
        assertNotNull(response);     // comprueba que la lista no es nula (existen registros)
        assertEquals(2, response.getBody().size()); // comprueba que ahora hay 2 registros
    }

    // comprueba que al intentar eliminar un registro no existente (id=4) da un error
    @Test
    public void deleteWithNotExistsIdShouldInternalError() {

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + NEW_CLIENT_ID, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());   // comprueba el tipo de error
    }

    public static final String EXIST_CLIENT_NAME = "Juana García";

    // comprueba que al intentar insertar un cliente con nombre que ya existe da error
    @Test
    public void addWithNameThatExistsReturnsError() {

        ClientDto dto = new ClientDto();
        dto.setName(EXIST_CLIENT_NAME);

        ResponseEntity<Void> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

}

