package com.ccsw.tutorial.author;

import com.ccsw.tutorial.author.model.Author;
import com.ccsw.tutorial.author.model.AuthorDto;
import com.ccsw.tutorial.author.model.AuthorSearchDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ccsw
 *
 */
@Service
@Transactional
public class AuthorServiceImpl implements AuthorService {

    @Autowired
    AuthorRepository authorRepository;

    @Override
    public List<Author> findAll() {
        return (List<Author>) this.authorRepository.findAll();
    }

    // METODO QUE DEVUELVE EL AUTOR CON EL ID QUE SE LE PASA
    @Override
    public Author get(Long id) {
        return this.authorRepository.findById(id).orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<Author> findPage(AuthorSearchDto dto) {

        // PageRequest es una clase q facilita la creacion de objetos Pageable y especificar la informacion de paginacion
        Pageable pageable = PageRequest.of(dto.getPageable().getPageNumber(), dto.getPageable().getPageSize());

        return this.authorRepository.findAll(pageable);     // devuelve una lista de autores en la pagina solicitada
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(Long id, AuthorDto data) {

        Author author;

        if (id == null) {
            author = new Author();
        } else {
            author = this.get(id);
            //author = this.authorRepository.findById(id).orElse(null); --> esta linea se cambia por la anterior
            //
        }

        BeanUtils.copyProperties(data, author, "id");  // copia las propiedades del objeto dato (tipo dto)
        // a author (tipo entidad, Author), excepto el id q no queremos q lo copie como null

        this.authorRepository.save(author);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Long id) throws Exception {

        if (this.get(id) == null) {
            throw new Exception("Not exists");
        }

        this.authorRepository.deleteById(id);
    }

}
