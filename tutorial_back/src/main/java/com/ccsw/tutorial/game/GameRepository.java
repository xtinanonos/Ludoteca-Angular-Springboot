package com.ccsw.tutorial.game;

import com.ccsw.tutorial.game.model.Game;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author ccsw
 *
 */
public interface GameRepository extends CrudRepository<Game, Long>, JpaSpecificationExecutor<Game> {
    //las siguientes lineas las añadimos para que SpringData haga una unica consulta con la correspondiente transaccion
    // con la BBDD en vez de lanzar multiples queries
    @Override
    @EntityGraph(attributePaths = { "category", "author" })
    // atributos q se incluyen en la consulta principal con joins
    List<Game> findAll(Specification<Game> spec);
}
