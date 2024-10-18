package com.ccsw.tutorial.author;

// CLASE CONTROLADOR DE AUTOR --> RECIBE LAS PETICIONES DE SERVICIO DEL CLIENTE, Y LE DEVUELVE LA RESPUESTA
// ES LA QUE TIENE LOS ENDPOINTS DE ENTRADA A LA APLICACION

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDto;
import com.ccsw.tutorial.author.model.AuthorSearchDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author ccsw
 *
 */
@Tag(name = "Author", description = "API of Author")
@RequestMapping(value = "/author")
@RestController     // permite procesar solicitudes http y devolver respuestas en JSON o XML
@CrossOrigin(origins = "*")
public class AuthorController {

    @Autowired
    AuthorService authorService;

    @Autowired
    ModelMapper mapper;

    /**
     * Recupera un listado de autores {@link Author}
     *
     * @return {@link List} de {@link AuthorDto}
     */
    @Operation(summary = "Find", description = "Method that return a list of Authors")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<AuthorDto> findAll() {

        List<Author> authors = this.authorService.findAll();

        return authors.stream().map(e -> mapper.map(e, AuthorDto.class)).collect(Collectors.toList());
    }

    /**
     * Método para recuperar un listado paginado de {@link Author}
     *
     * @param dto dto de búsqueda
     * @return {@link Page} de {@link AuthorDto}
     */
    @Operation(summary = "Find Page", description = "Method that return a page of Authors")
    @RequestMapping(path = "", method = RequestMethod.POST)
    public Page<AuthorDto> findPage(@RequestBody AuthorSearchDto dto) {

        Page<Author> page = this.authorService.findPage(dto);

        /*convierte la coleccion de elementos de la pagina donde se encuentra el autor y la convierte
         * en un flujo. Transforma cada elemento e del flujo (elemento entidad) a un elemento AuthorDto(modelo dto)
         * A continuacion, hace una lista de todos los elementos y obtiene info de la pag actual y del
         * numero total de paginas*/
        return new PageImpl<>(page.getContent().stream().map(e -> mapper.map(e, AuthorDto.class)).collect(Collectors.toList()), page.getPageable(), page.getTotalElements());
    }

    /**
     * Método para crear o actualizar un {@link Author}
     *
     * @param id PK de la entidad
     * @param dto datos de la entidad
     */
    @Operation(summary = "Save or Update", description = "Method that saves or updates a Author")
    @RequestMapping(path = { "", "/{id}" }, method = RequestMethod.PUT)
    public void save(@PathVariable(name = "id", required = false) Long id, @RequestBody AuthorDto dto) {

        this.authorService.save(id, dto);
    }

    /**
     * Método para eliminar un {@link Author}
     *
     * @param id PK de la entidad
     */
    @Operation(summary = "Delete", description = "Method that deletes a Author")
    @RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("id") Long id) throws Exception {

        this.authorService.delete(id);
    }

}