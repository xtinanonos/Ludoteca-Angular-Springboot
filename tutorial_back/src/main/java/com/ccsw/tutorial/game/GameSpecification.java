package com.ccsw.tutorial.game;
// CLASE QUE CONTIENE LA CONSTRUCCION DE LA CONSULTA DE FILTRADO SEGUN LOS CRITERIOS QUE SE LE PROPORCIONAN
// SE DEBE CREAR UNA CLASE SPECIFICATION PARA CADA UNA DE LAS ENTITY QUE QUERAMOS CONSULTAR DE FORMA FILTRADA

import com.ccsw.tutorial.common.criteria.SearchCriteria;
import com.ccsw.tutorial.game.model.Game;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public class GameSpecification implements Specification<Game> {

    private static final long serialVersionUID = 1L;

    private final SearchCriteria criteria;      // debe generar un predicado (necesita unos criterios de filtrado)

    public GameSpecification(SearchCriteria criteria) {

        this.criteria = criteria;
    }

    @Override
    public Predicate toPredicate(Root<Game> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        //en la siguiente linea se establece el tipo de operacion, en este caso será de comparacion(:),
        // pero tambien podria ser otro tipo de operaciones como (<,>,<>)
        if (criteria.getOperation().equalsIgnoreCase(":") && criteria.getValue() != null) {
            Path<String> path = getPath(root);
            if (path.getJavaType() == String.class) {       // si es un texto
                return builder.like(path, "%" + criteria.getValue() + "%"); // ruta%valor comparado%
            } else {        // si es un numero o fecha
                return builder.equal(path, criteria.getValue());
            }
        }
        return null;
    }

    //nos permite explorar las subentidades (autor y categoria) para realizar consultas sobre los atributos de estas
    // por ejemplo el nombre del autor
    private Path<String> getPath(Root<Game> root) { // path es un tipo en Criteria API que se refiere a una propiedad de una entidad
        String key = criteria.getKey(); // obtiene el campo (game.category.name)
        String[] split = key.split("[.]", 0);   // la clave se divide en array de cadenas --> {game,category,name}

        Path<String> expression = root.get(split[0]);   // se obtiene la primera parte, que representa la entidad principal --> game
        for (int i = 1; i < split.length; i++) {    // se recorre la clave empezando en posicion 1 (category, name)
            expression = expression.get(split[i]);
        }

        return expression;  // path q apunta a la propiedad 'name' de 'categoria' de 'game' --> game.getCategory().getName();
        // el motivo por el que no se pone directamente game.getCategory().getName(); es por si se produce una modificacion
        // de los nombres de algunas de las variables.
    }

}
