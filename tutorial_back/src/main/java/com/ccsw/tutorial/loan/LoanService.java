package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import org.springframework.data.domain.Page;

public interface LoanService {

    /**
     * Recupera un listado de {@link Loan} filtrando opcionalmente por juego, cliente y fecha, con soporte para paginación.
     * @return {@link Page} de {@link Loan}
     */
    Page<Loan> find(LoanSearchDto dto);

    /**
     * Recupera un {@link Loan} a través de su ID
     * @param id PK
     * @return {@link Loan}
     */
    Loan get(Long id);

    /**
     * Método para crear un {@link Loan}
     * @param dto datos de la entidad
     */
    void save(LoanDto dto) throws ValidationException;

    /**
     * Método para eliminar un {@link Loan}
     * @param id PK de la entidad
     */
    void delete(Long id) throws Exception;

}
