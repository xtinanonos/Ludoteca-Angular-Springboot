package com.ccsw.tutorial.loan;

// CLASE QUE CONTIENE LA CONSTRUCCION DE LA CONSULTA DE FILTRADO SEGUN LOS CRITERIOS QUE SE LE PROPORCIONAN

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.loan.model.Loan;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class LoanSpecification implements Specification<Loan> {

    private static final long serialVersionUID = 1L;

    private final SearchCriteria criteria;  // criterios de filtrado

    public LoanSpecification(SearchCriteria criteria) {
        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Loan> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        // Filtrado por rango de fechas
        if (criteria.getKey().equals("date_start") && criteria.getOperation().equals("<=") && criteria.getValue() instanceof LocalDate) {
            LocalDate filterDate = (LocalDate) criteria.getValue();
            return builder.lessThanOrEqualTo(root.get("date_start"), filterDate);
        }

        if (criteria.getKey().equals("date_end") && criteria.getOperation().equals(">=") && criteria.getValue() instanceof LocalDate) {
            LocalDate filterDate = (LocalDate) criteria.getValue();
            return builder.greaterThanOrEqualTo(root.get("date_end"), filterDate);
        }

        // Filtrado por otros criterios (nombre del juego e id del cliente)
        if (criteria.getOperation().equalsIgnoreCase(":") && criteria.getValue() != null) {
            Path<String> path = getPath(root);  // name(string) o id(Long)
            if (path.getJavaType() == String.class) { // si es un texto
                return builder.like(path, "%" + criteria.getValue() + "%");
            } else { // si es un número o fecha
                return builder.equal(path, criteria.getValue());
            }
        }

        return null;
    }

    // Nos permite explorar las subentidades (game y client) para realizar consultas sobre los atributos de estas
    private Path<String> getPath(Root<Loan> root) { // path es un tipo en Criteria API que se refiere a una propiedad de una entidad
        String key = criteria.getKey(); // obtiene el campo (loan.game.name)
        String[] split = key.split("[.]", 0); // la clave se divide en array de cadenas --> {loan,game,name}

        Path<String> expression = root.get(split[0]); // se obtiene la primera parte, que representa la entidad principal --> loan
        for (int i = 1; i < split.length; i++) { // se recorre la clave empezando en posición 1 (game, name)
            expression = expression.get(split[i]);
        }

        return expression; // path que apunta a la propiedad 'name' de 'game' de 'loan' --> loan.getGame().getName();
    }
}
