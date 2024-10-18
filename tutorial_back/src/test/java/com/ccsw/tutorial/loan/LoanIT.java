package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.model.ClientDto;
import com.ccsw.tutorial.common.pagination.PageableRequest;
import com.ccsw.tutorial.config.ResponsePage;
import com.ccsw.tutorial.game.model.GameDto;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
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

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)

public class LoanIT {

    public static final String LOCALHOST = "http://localhost:";
    public static final String SERVICE_PATH = "/loan";

    public static final Long DELETE_LOAN_ID = 6L;
    public static final Long MODIFY_LOAN_ID = 3L;

    private static final int TOTAL_LOANS = 6;
    private static final int PAGE_SIZE = 5;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    ParameterizedTypeReference<ResponsePage<LoanDto>> responseTypePage = new ParameterizedTypeReference<ResponsePage<LoanDto>>() {
    };

    @Test
    public void findLoansWithFiltersShouldReturnCorrectLoans() {
        // Configura el Pageable para la página 0 con un tamaño de página de 5
        PageableRequest pageableRequest = new PageableRequest(0, 5);

        // Crea un searchDto con filtros para la búsqueda
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(pageableRequest);
        searchDto.setGameName("On Mars");
        searchDto.setIdClient(2L);
        searchDto.setDate(LocalDate.parse("2024-10-10"));

        // Ejecuta la solicitud HTTP simulada
        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        // Verifica la respuesta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getTotalElements());   // solo espera 1 elemento q coincida con los filtros aplicados
    }

    @Test
    public void findLoansWithPaginationShouldReturnCorrectPage() {
        // Configura el Pageable para la página 0 con un tamaño de página de 5
        PageableRequest pageableRequest = new PageableRequest(0, 5);

        // Crea un searchDto sin filtros para la búsqueda
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(pageableRequest);

        // Ejecuta la solicitud HTTP simulada
        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        // Verifica la respuesta
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TOTAL_LOANS, response.getBody().getTotalElements());
        assertEquals(PAGE_SIZE, response.getBody().getContent().size());
        assertEquals(2, response.getBody().getTotalPages());
    }

    @Test
    public void saveShouldCreateNewLoans() throws Exception {
        long newLoanId = TOTAL_LOANS + 1;
        long newLoanSize = TOTAL_LOANS + 1;

        LoanDto loanDto = new LoanDto();
        ClientDto clientDto = new ClientDto();
        GameDto gameDto = new GameDto();

        clientDto.setId(1L);
        gameDto.setId(1L);

        loanDto.setClient(clientDto);
        loanDto.setGame(gameDto);
        loanDto.setDate_start(LocalDate.of(2020, 5, 1));  // Fecha de inicio
        loanDto.setDate_end(LocalDate.of(2020, 5, 10));    // Fecha de fin (menos de 14 días)

        // Realiza la solicitud PUT con el DTO del préstamo
        ResponseEntity<String> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(loanDto), String.class);

        // Verifica que la respuesta sea la esperada
        assertEquals(HttpStatus.OK, response.getStatusCode());

        // Realiza una búsqueda para verificar que el préstamo se ha guardado
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, 5));
        ResponseEntity<ResponsePage<LoanDto>> loanResponse = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);

        assertNotNull(loanResponse.getBody());
        assertEquals(newLoanSize, loanResponse.getBody().getTotalElements());  // Comprueba que ha aumentado el numero de prestamos guardados
    }

    @Test
    public void saveShouldFailWhenLoanPeriodIsInvalid() throws Exception {
        LoanDto dto = new LoanDto();
        ClientDto clientDto = new ClientDto();
        GameDto gameDto = new GameDto();

        clientDto.setId(1L);
        gameDto.setId(1L);

        dto.setClient(clientDto);
        dto.setGame(gameDto);
        dto.setDate_start(LocalDate.of(2024, 12, 1));
        dto.setDate_end(LocalDate.of(2024, 12, 16));  // Periodo de más de 14 días

        // Realiza la solicitud PUT con periodo de préstamo inválido
        ResponseEntity<String> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), String.class);

        // Verifica que la respuesta sea BAD_REQUEST y el mensaje de error sea el esperado
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El periodo del préstamo no es válido", response.getBody());
    }

    @Test
    public void saveShouldFailWhenGameIsNotAvailable() throws Exception {
        LoanDto dto = new LoanDto();
        ClientDto clientDto = new ClientDto();
        GameDto gameDto = new GameDto();

        clientDto.setId(6L);
        gameDto.setId(1L);

        dto.setClient(clientDto);
        dto.setGame(gameDto);
        dto.setDate_start(LocalDate.of(2024, 10, 14));
        dto.setDate_end(LocalDate.of(2024, 10, 24));  // Fecha en la que el juego con id 1 no está disponible

        // Realiza la solicitud PUT con juego no disponible
        ResponseEntity<String> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), String.class);

        // Verifica que la respuesta sea BAD_REQUEST y el mensaje de error sea el esperado
        //        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El juego no está disponible en el periodo seleccionado", response.getBody());
    }

    @Test
    public void saveShouldFailWhenClientHasOtherLoansInSamePeriod() throws Exception {
        LoanDto dto = new LoanDto();
        ClientDto clientDto = new ClientDto();
        GameDto gameDto = new GameDto();

        clientDto.setId(1L);  // Cliente con préstamos en el mismo periodo
        gameDto.setId(3L);

        dto.setClient(clientDto);
        dto.setGame(gameDto);
        dto.setDate_start(LocalDate.of(2024, 10, 5));  // Fecha de inicio en la que ya tiene un préstamo
        dto.setDate_end(LocalDate.of(2024, 10, 10));   // Fecha de fin en la que ya tiene un préstamo
        // Realiza la solicitud PUT con cliente que ya tiene préstamos en el mismo periodo
        ResponseEntity<String> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.PUT, new HttpEntity<>(dto), String.class);

        // Verifica que la respuesta sea BAD_REQUEST y el mensaje de error sea el esperado
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("El cliente ya tiene préstamos en el mismo periodo", response.getBody());
    }

    @Test
    public void deleteWithExistsIdShouldDeleteLoan() {

        long newLoansSize = TOTAL_LOANS - 1;

        // prueba la eliminacion
        restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + DELETE_LOAN_ID, HttpMethod.DELETE, null, Void.class);

        // establece el numero de paginas y de registros por pagina
        LoanSearchDto searchDto = new LoanSearchDto();
        searchDto.setPageable(new PageableRequest(0, TOTAL_LOANS));

        ResponseEntity<ResponsePage<LoanDto>> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH, HttpMethod.POST, new HttpEntity<>(searchDto), responseTypePage);
        assertNotNull(response);

        // comprueba que el numero total de elementos registrados es igual al esperado
        assertEquals(newLoansSize, response.getBody().getTotalElements());
    }

    @Test
    public void deleteWithNotExistsIdShouldThrowException() {

        long deleteAuthorId = TOTAL_LOANS + 1;

        ResponseEntity<?> response = restTemplate.exchange(LOCALHOST + port + SERVICE_PATH + "/" + deleteAuthorId, HttpMethod.DELETE, null, Void.class);

        // comprueba que da un error al intentar eliminar un id inexistente
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    ParameterizedTypeReference<List<LoanDto>> responseTypeList = new ParameterizedTypeReference<List<LoanDto>>() {
    };

}
