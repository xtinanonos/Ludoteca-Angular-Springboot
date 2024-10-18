package com.ccsw.tutorial.category;
// TEST DE INTEGRACION DE CATEGORY

import com.ccsw.tutorial.category.model.CategoryDto;
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

public class CategoryIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/category";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<List<CategoryDto>> responseType = new ParameterizedTypeReference<List<CategoryDto>>() {
    };

    //comprobamos que el findAll devuelve todas las categorías
    @Test
    public void findAllShouldReturnAllCategories() {

        ResponseEntity<List<CategoryDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);

        assertNotNull(response);
        assertEquals(3, response.getBody().size());
    }

    public static final Long NEW_CATEGORY_ID = 4L;
    public static final String NEW_CATEGORY_NAME = "CAT4";

    // comprobamos que al hacer save con id nulo, crea una nueva categoría
    @Test
    public void saveWithoutIdShouldCreateNewCategory() {

        CategoryDto dto = new CategoryDto();
        dto.setName(NEW_CATEGORY_NAME);

        //endpoint para añadir la categoria
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        //endpoint para buscar la categoria, si la respuesta no es nula, se ha guardado correctamente
        ResponseEntity<List<CategoryDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
        assertNotNull(response);
        assertEquals(4, response.getBody().size()); // deberia haber 4 categorias (las añadidas por el data.sql y la del test)

        CategoryDto categorySearch = response.getBody().stream().filter(item -> item.getId().equals(NEW_CATEGORY_ID)).findFirst().orElse(null); // comprueba que el id es 4
        assertNotNull(categorySearch);
        assertEquals(NEW_CATEGORY_NAME, categorySearch.getName());      //comprueba que el nombre se ha guardado bien
    }

    public static final Long MODIFY_CATEGORY_ID = 3L;       // id a modificar

    // comprueba que una peticion de modificar un registro con id que existe funciona
    @Test
    public void modifyWithExistIdShouldModifyCategory() {

        CategoryDto dto = new CategoryDto();
        dto.setName(NEW_CATEGORY_NAME);

        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + MODIFY_CATEGORY_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        ResponseEntity<List<CategoryDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
        assertNotNull(response);
        assertEquals(3, response.getBody().size());     // comprueba que el listado sigue siendo 3 y no se ha añadido un nuevo registro

        CategoryDto categorySearch = response.getBody().stream().filter(item -> item.getId().equals(MODIFY_CATEGORY_ID)).findFirst().orElse(null);
        assertNotNull(categorySearch);
        assertEquals(NEW_CATEGORY_NAME, categorySearch.getName());
    }

    //comprueba que dé un internal error si se intenta modificar un registro con id inexistente
    @Test
    public void modifyWithNotExistIdShouldInternalError() {

        CategoryDto dto = new CategoryDto();
        dto.setName(NEW_CATEGORY_NAME);

        //busca el id 4, que no existe
        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + NEW_CATEGORY_ID, HttpMethod.PUT, new HttpEntity<>(dto), Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());       // comprueba que el error sea un internal error
    }

    // comprueba que se llame al metodo delete y que despues hay un registro menos
    public static final Long DELETE_CATEGORY_ID = 2L;

    @Test
    public void deleteWithExistsIdShouldDeleteCategory() {

        // simula la accion de delete
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_CATEGORY_ID, HttpMethod.DELETE, null, Void.class);

        ResponseEntity<List<CategoryDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.GET, null, responseType);
        assertNotNull(response);     // comprueba que la lista no es nula (existen registros)
        assertEquals(2, response.getBody().size()); // comprueba que ahora hay 2 registros
    }

    // comprueba que al intentar eliminar un registro no existente (id=4) da un error
    @Test
    public void deleteWithNotExistsIdShouldInternalError() {

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + NEW_CATEGORY_ID, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());   // comprueba el tipo de error
    }
}

