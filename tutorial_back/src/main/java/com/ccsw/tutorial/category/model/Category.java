package com.ccsw.tutorial.category.model;

/* clase entity para persistir y recuperar datos persistentes. CAPA DE BASE DE DATOS
es una clase con caracteristicas muy similares al DTO (atributos, getter y setters,...),
pero a diferencia de esta tiene una serie de anotaciones (@Entity, @Table, @Id y @GeneratedValue, "@Column...)
que permiten a JPA generar consultas SQL a la BBDD*/

import jakarta.persistence.*;

/**
 * @author ccsw
 *
 */
@Entity                 // indica que implementa una entidad de BBDD, permite hacer queries
@Table(name = "category")           // indica a JPA el nombre de la tabla
public class Category {

    // Columna ID que es primary key
    @Id                                                   // indica que es PK
    @GeneratedValue(strategy = GenerationType.IDENTITY)   // indica la estrategia para generar la PK
    @Column(name = "id", nullable = false)      // indica que mapea una propiedad como columna e indica su nombre
    private Long id;

    // Columna name (nombre)
    @Column(name = "name", nullable = false)
    private String name;

    /**
     * @return id
     */
    public Long getId() {

        return this.id;
    }

    /**
     * @param id new value of {@link #getId}.
     */
    public void setId(Long id) {

        this.id = id;
    }

    /**
     * @return name
     */
    public String getName() {

        return this.name;
    }

    /**
     * @param name new value of {@link #getName}.
     */
    public void setName(String name) {

        this.name = name;
    }

}
