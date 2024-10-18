package com.ccsw.tutorial.category;
//EL ACCESO A DATOS SE DEBE HACER SIEMPRE DESDE UN REPOSITORY
// se puede hacer utilizando desde el automatico de JPA hasta uno manual

import com.ccsw.tutorial.category.model.Category;
import org.springframework.data.repository.CrudRepository;

/**
 * @author ccsw
 *
 */

//SPRING TIENE POR DEFECTO UN REPOSITORIO PARA OPERACIONES CRUD, SOLO HAY QUE EXTENDERLO

public interface CategoryRepository extends CrudRepository<Category, Long> {

}
