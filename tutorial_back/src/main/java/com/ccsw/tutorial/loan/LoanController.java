package com.ccsw.tutorial.loan;

import com.ccsw.tutorial.loan.model.Loan;
import com.ccsw.tutorial.loan.model.LoanDto;
import com.ccsw.tutorial.loan.model.LoanSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * @crbolano ccsw
 *
 */

@Tag(name = "Loan", description = "API of Loan")
@RequestMapping(value = "/loan")
@RestController
@CrossOrigin(origins = "*")
public class LoanController {

    @Autowired
    LoanService loanService;

    @Autowired
    ModelMapper mapper;

    /**
     * Recupera un listado de prestamos paginados y filtrados {@link Loan}
     * @return {@link Page} de {@link LoanDto}
     */
    @Operation(summary = "Find Loans", description = "Method that returns a filtered and/or paginated list of Loans")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<LoanDto> findLoans(@RequestBody LoanSearchDto loanSearchDto) {

        Page<Loan> page = loanService.find(loanSearchDto);       // Llama al servicio pasandole los parametros de busqueda

        // Transformar a LoanDto y devolver paginacion de elementos
        return new PageImpl<>(page.getContent().stream().map(e -> mapper.map(e, LoanDto.class)).collect(Collectors.toList()), page.getPageable(), page.getTotalElements());
    }

    /**
     * Método para controlar las excepciones de tipo ValidationException en el método Save
     * @return {@link ResponseEntity} de {@link String}
     * @param ex mensaje de error
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    /**
     * Método para crear un {@link Loan}
     * @param dto datos de la entidad
     */
    @Operation(summary = "Save", description = "Method that saves a Loan")
    @RequestMapping(path = "", method = RequestMethod.PUT)
    public void save(@RequestBody LoanDto dto) throws ValidationException {
        this.loanService.save(dto);
    }

    /**
     * Método para eliminar un {@link Loan}
     *
     * @param id PK de la entidad
     */
    @Operation(summary = "Delete", description = "Method that deletes a Loan")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {

        this.loanService.delete(id);
    }

}
