package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.client.ClientService;
import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.GameService;
import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LoanServiceImpl implements LoanService {
    @Autowired
    LoanRepository loanRepository;

    @Autowired
    GameService gameService;

    @Autowired
    ClientService clientService;

    @Override
    public Page<Loan> find(LoanSearchDto dto) {

        Pageable pageable = PageRequest.of(dto.getPageable().getPageNumber(), dto.getPageable().getPageSize());
        String gameName = dto.getGameName();
        Long idClient = dto.getIdClient();
        LocalDate date = dto.getDate();

        Specification<Loan> specification = Specification.where(null); // Inicia la especificación vacía

        // Crea las especificaciones para cada parametro de filtrado:

        // Agrega condición para el nombre del juego
        if (gameName != null && !gameName.isEmpty()) {
            SearchCriteria gameCriteria = new SearchCriteria("game.title", ":", gameName);
            LoanSpecification gameSpec = new LoanSpecification(gameCriteria);
            specification = specification.and(gameSpec);
        }

        // Agrega condición para el ID del cliente
        if (idClient != null) {
            SearchCriteria clientCriteria = new SearchCriteria("client.id", ":", idClient);
            LoanSpecification clientSpec = new LoanSpecification(clientCriteria);
            specification = specification.and(clientSpec);
        }

        // Agrega condición para la fecha
        if (date != null) {
            // Filtra por el rango de fechas: date_start <= date y date_end >= date
            SearchCriteria startDateCriteria = new SearchCriteria("date_start", "<=", date);
            LoanSpecification startDateSpec = new LoanSpecification(startDateCriteria);
            specification = specification.and(startDateSpec);

            SearchCriteria endDateCriteria = new SearchCriteria("date_end", ">=", date);
            LoanSpecification endDateSpec = new LoanSpecification(endDateCriteria);
            specification = specification.and(endDateSpec);
        }

        // Realiza la búsqueda en el repositorio utilizando la especificación y la paginación
        return loanRepository.findAll(specification, pageable);
    }

    @Override
    public Loan get(Long id) {
        return this.loanRepository.findById(id).orElse(null);
    }

    @Override
    public void save(LoanDto dto) throws ValidationException {
        Loan loan = new Loan();

        //copia la informacion del dto (controller) al loan (entidad bbdd), excepto el id, el game y el client
        BeanUtils.copyProperties(dto, loan, "id", "game", "client");

        // en su lugar, se llama al id de game y al id de client
        loan.setGame(gameService.get(dto.getGame().getId()));
        loan.setClient(clientService.get(dto.getClient().getId()));

        List<String> errorMessages = new ArrayList<>();

        // Validaciones --> esta forma de ir añadiendo los mensajes en un array de strings sirve
        // para que se puedan validar todos los campos, y envie todos los mensajes de error.

        if (!validateLoanPeriod(loan)) {
            errorMessages.add("El periodo del préstamo no es válido.");
        }
        if (!validateGameIsAvailable(loan)) {
            errorMessages.add("El juego no está disponible en el período seleccionado.");
        }
        if (!validateClientLoans(loan)) {
            errorMessages.add("El cliente ya tiene préstamos en el mismo período.");
        }

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(String.join(", ", errorMessages));  // Lanza todos los errores en un solo mensaje
        }

        // Guardar el préstamo
        this.loanRepository.save(loan);
    }

    // comprueba que el cliente no tenga préstamos en el período seleccionado
    private boolean validateClientLoans(Loan loan) {
        List<Loan> loansForClient = loanRepository.findByClientIdAndDateRange(loan.getClient().getId(), loan.getDate_start(), loan.getDate_end());

        return loansForClient.isEmpty();
    }

    // comprueba que el juego esté disponible en el período seleccionado
    private boolean validateGameIsAvailable(Loan loan) {
        List<Loan> loansForGame = loanRepository.findByGameIdAndDateRange(loan.getGame().getId(), loan.getDate_start(), loan.getDate_end());

        return loansForGame.isEmpty();
    }

    // comprueba que el período sea válido
    private boolean validateLoanPeriod(Loan loan) {
        // Verifica que la fecha de inicio no sea posterior a la fecha de fin
        if (loan.getDate_start().isAfter(loan.getDate_end())) {
            return false;
        }
        // metodo ChronoUnit para calcular diferencia entre dos fechas
        long diasPrestamo = ChronoUnit.DAYS.between(loan.getDate_start(), loan.getDate_end());
        return diasPrestamo <= 14;
    }

    @Override
    public void delete(Long id) throws Exception {
        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        this.loanRepository.deleteById(id);
    }
}
